package org.fastorm.writer.impl;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.context.IContext;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMutableMakerAndEntityDefnVisitor;
import org.fastorm.temp.IMutatingTempTableMaker;
import org.fastorm.writer.IEntityWriterThin;

public class EntityWriterThin implements IEntityWriterThin {

	private final IMutableMakerAndEntityDefnVisitor createTempTables(final IContext writeContext) {
		return new IMutableMakerAndEntityDefnVisitor() {
			@Override
			public void accept(IMutatingTempTableMaker maker, IEntityDefn entityDefn) throws Exception {
				maker.create(writeContext, entityDefn);

			}
		};
	}

	private final IMutableMakerAndEntityDefnVisitor populateUpdateTempTables(final IContext writeContext) {
		return new IMutableMakerAndEntityDefnVisitor() {
			@Override
			public void accept(IMutatingTempTableMaker maker, IEntityDefn entityDefn) throws Exception {
				IDrainedTableData table = writeContext.get(entityDefn);
				maker.populateUpdate(writeContext, table);
			}
		};
	}

	private final IMutableMakerAndEntityDefnVisitor update(final IContext writeContext) {
		return new IMutableMakerAndEntityDefnVisitor() {
			@Override
			public void accept(IMutatingTempTableMaker maker, IEntityDefn entityDefn) throws Exception {
				maker.update(writeContext, entityDefn);
			}
		};
	};

	@Override
	public void write(IContext context) {
		writeUpdates(context);
		writeDeletes(context);
		writeInserts(context);

	}

	private void writeUpdates(IContext context) {
		IFastOrmContainer fastOrm = context.getFastOrm();
		IEntityDefn.Utils.walk(fastOrm, createTempTables(context));
		IEntityDefn.Utils.walk(fastOrm, populateUpdateTempTables(context));
		IEntityDefn.Utils.walk(fastOrm, update(context));
	}

	private void writeDeletes(IContext context) {
	}

	private void writeInserts(IContext context) {
	}

}
