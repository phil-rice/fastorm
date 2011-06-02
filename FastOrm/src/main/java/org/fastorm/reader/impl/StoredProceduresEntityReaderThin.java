package org.fastorm.reader.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.impl.DataSetBuilder;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMakerAndEntityDefnFoldVisitor;
import org.fastorm.defns.IMakerAndEntityDefnVisitor;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.collections.AbstractFindNextIterable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

public class StoredProceduresEntityReaderThin implements IEntityReaderThin {
	private boolean createdOnce;

	@Override
	public <T> Iterable<IDataSet> dataSets(final IFastOrmContainer fastOrm) {
		return new AbstractFindNextIterable<IDataSet, AtomicInteger>() {

			private IDataSet last = null;

			@Override
			protected IDataSet findNext(AtomicInteger context) {
				final int page = context.getAndIncrement();
				IMemoryManager memoryManager = fastOrm.getContainer().getMemoryManager();
				memoryManager.dispose();

				IDataSet result = fastOrm.getJdbcTemplate().execute(new ConnectionCallback<IDataSet>() {
					@Override
					public IDataSet doInConnection(Connection connection) throws SQLException, DataAccessException {
						final OrmReadContext ormReadContext = new OrmReadContext(connection);
						class DropAndCreateStoredProcedures implements IMakerAndEntityDefnVisitor {
							@Override
							public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								maker.dropStoredProcedure(fastOrm, ormReadContext);
								maker.createStoredProcedure(fastOrm, ormReadContext);
							}

							@Override
							public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								maker.dropStoredProcedure(fastOrm, ormReadContext, parent, child);
								maker.createStoredProcedure(fastOrm, ormReadContext, parent, child);
							}
						}

						class CreateTempTables implements IMakerAndEntityDefnVisitor {
							@Override
							public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								maker.drop(fastOrm, ormReadContext);
								maker.create(fastOrm, ormReadContext);
							}

							@Override
							public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								maker.drop(fastOrm, ormReadContext, child);
								maker.create(fastOrm, ormReadContext, parent, child);
							}
						}

						class CallStoredProcs implements IMakerAndEntityDefnFoldVisitor<IDrainedTableData, IDataSet> {

							@Override
							public IDrainedTableData acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								return maker.drainFromStoredProcedure(fastOrm, ormReadContext, page);
							}

							@Override
							public IDrainedTableData acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								return maker.drainFromStoredProcedure(fastOrm, ormReadContext, parent, child);
							}
						}
						ICallback<Long> total = new ICallback<Long>() {
							@Override
							public void process(Long t) throws Exception {
								fastOrm.getSqlLogger().total(t);
							}
						};
						if (page == 0) {
							if (!createdOnce || fastOrm.getOptions().createAnddropProceduresAtStartOfRun) {
								IEntityDefn.Utils.walk(fastOrm, new CreateTempTables());
								IEntityDefn.Utils.walk(fastOrm, new DropAndCreateStoredProcedures());
								createdOnce = true;
							}
						}
						last = IEntityDefn.Utils.aggregateAndTime(fastOrm, new CallStoredProcs(), new DataSetBuilder(), total);
						if (last.getPrimaryTable().size() == 0)
							return null;
						else
							return last;
					}
				});
				return result;
			}

			@Override
			protected AtomicInteger reset() throws Exception {
				return new AtomicInteger();
			}
		};
	}

}
