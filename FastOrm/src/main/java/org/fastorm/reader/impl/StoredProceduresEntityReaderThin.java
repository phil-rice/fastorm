package org.fastorm.reader.impl;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.context.IContext;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMakerAndEntityDefnVisitor;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;

public class StoredProceduresEntityReaderThin implements IEntityReaderThin {
	private boolean createdOnce;

	@Override
	public IMutableDataSet readPage(final int page, final IContext context) {
		final IFastOrmContainer fastOrm = context.getFastOrm();
		IMemoryManager memoryManager = fastOrm.getMemoryManager();
		memoryManager.dispose();

		class DropAndCreateStoredProcedures implements IMakerAndEntityDefnVisitor {
			@Override
			public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
				maker.dropStoredProcedure(context);
				maker.createStoredProcedure(context);
			}

			@Override
			public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
				maker.dropStoredProcedure(context, parent, child);
				maker.createStoredProcedure(context, parent, child);
			}
		}

		class CreateTempTables implements IMakerAndEntityDefnVisitor {
			@Override
			public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
				maker.drop(context);
				maker.create(context);
			}

			@Override
			public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
				maker.drop(context, child);
				maker.create(context, parent, child);
			}
		}

		class CallStoredProcs implements IMakerAndEntityDefnVisitor {

			@Override
			public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
				maker.drainFromStoredProcedure(context, page);
			}

			@Override
			public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
				maker.drainFromStoredProcedure(context, parent, child);
			}
		}

		if (page == 0) {
			if (!createdOnce || fastOrm.createAnddropProceduresAtStartOfRun()) {
				IEntityDefn.Utils.walk(fastOrm, new CreateTempTables());
				IEntityDefn.Utils.walk(fastOrm, new DropAndCreateStoredProcedures());
				createdOnce = true;
			}
		}
		IEntityDefn.Utils.walk(fastOrm, new CallStoredProcs());
		IMutableDataSet dataSet = context.buildDataSet();
		return dataSet;
	}

}
