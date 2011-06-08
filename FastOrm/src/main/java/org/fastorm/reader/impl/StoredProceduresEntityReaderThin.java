package org.fastorm.reader.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.context.ReadContext;
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
						final ReadContext readContext = new ReadContext(fastOrm, connection);
						class DropAndCreateStoredProcedures implements IMakerAndEntityDefnVisitor {
							@Override
							public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								maker.dropStoredProcedure(fastOrm, readContext);
								maker.createStoredProcedure(fastOrm, readContext);
							}

							@Override
							public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								maker.dropStoredProcedure(fastOrm, readContext, parent, child);
								maker.createStoredProcedure(fastOrm, readContext, parent, child);
							}
						}

						class CreateTempTables implements IMakerAndEntityDefnVisitor {
							@Override
							public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								maker.drop(fastOrm, readContext);
								maker.create(fastOrm, readContext);
							}

							@Override
							public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								maker.drop(fastOrm, readContext, child);
								maker.create(fastOrm, readContext, parent, child);
							}
						}

						class CallStoredProcs implements IMakerAndEntityDefnVisitor {

							@Override
							public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								maker.drainFromStoredProcedure(fastOrm, readContext, page);
							}

							@Override
							public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								maker.drainFromStoredProcedure(fastOrm, readContext, parent, child);
							}
						}
						ICallback<Long> total = new ICallback<Long>() {
							@Override
							public void process(Long t) throws Exception {
								fastOrm.getSqlLogger().total(t);
							}
						};
						if (page == 0) {
							if (!createdOnce || fastOrm.createAnddropProceduresAtStartOfRun()) {
								IEntityDefn.Utils.walk(fastOrm, new CreateTempTables());
								IEntityDefn.Utils.walk(fastOrm, new DropAndCreateStoredProcedures());
								createdOnce = true;
							}
						}
						IEntityDefn.Utils.walk(fastOrm, new CallStoredProcs());
						last = IEntityDefn.Utils.aggregateAndTime(fastOrm, new IMakerAndEntityDefnFoldVisitor<IDrainedTableData, IDataSet>() {
							@Override
							public IDrainedTableData acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								return readContext.get(primary);
							}

							@Override
							public IDrainedTableData acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								return readContext.get(child);
							}
						}, new DataSetBuilder(), total);
						if (last.size() == 0)
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
