package org.fastorm.writer.impl;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.context.OrmWriteContext;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMutableMakerAndEntityDefnVisitor;
import org.fastorm.temp.IMutatingTempTableMaker;
import org.fastorm.writer.IEntityWriterThin;
import org.fastorm.writer.IMutatedDataSet;

public class EntityWriterThin implements IEntityWriterThin {

	private final IMutableMakerAndEntityDefnVisitor createTempTables(final OrmWriteContext writeContext) {
		return new IMutableMakerAndEntityDefnVisitor() {
			@Override
			public void accept(IMutatingTempTableMaker maker, IEntityDefn entityDefn) throws Exception {
				maker.create(writeContext, entityDefn);

			}
		};
	}

	private final IMutableMakerAndEntityDefnVisitor populateUpdateTempTables(final OrmWriteContext writeContext) {
		return new IMutableMakerAndEntityDefnVisitor() {
			private final IMutatedDataSet dataSet = writeContext.getDataSet();

			@Override
			public void accept(IMutatingTempTableMaker maker, IEntityDefn entityDefn) throws Exception {
				IMutatedDataTable table = dataSet.getTableFor(entityDefn);
				maker.populateUpdate(writeContext, table);
			}
		};
	}

	private final IMutableMakerAndEntityDefnVisitor update(final OrmWriteContext writeContext) {
		return new IMutableMakerAndEntityDefnVisitor() {
			@Override
			public void accept(IMutatingTempTableMaker maker, IEntityDefn entityDefn) throws Exception {
				maker.update(writeContext, entityDefn);
			}
		};
	};

	@Override
	public void write(OrmWriteContext context) {
		writeUpdates(context);
		writeDeletes(context);
		writeInserts(context);

	}

	private void writeUpdates(OrmWriteContext context) {
		IFastOrmContainer fastOrm = context.getFastOrm();
		IEntityDefn.Utils.walk(fastOrm, createTempTables(context));
		IEntityDefn.Utils.walk(fastOrm, populateUpdateTempTables(context));
		IEntityDefn.Utils.walk(fastOrm, update(context));
	}

	private void writeDeletes(OrmWriteContext context) {
	}

	private void writeInserts(OrmWriteContext context) {
	}

}
