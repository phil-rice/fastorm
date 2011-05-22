package org.fastorm.reader.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.impl.DataSetBuilder;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMakerAndEntityDefnFoldVisitor;
import org.fastorm.defns.IMakerAndEntityDefnVisitor;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.AbstractFindNextIterable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

public class EntityReaderThin implements IEntityReaderThin {

	@Override
	public <T> Iterable<IDataSet> dataSets(final IFastOrmContainer fastOrm) {
		return new AbstractFindNextIterable<IDataSet, AtomicInteger>() {

			private IDataSet last = null;

			@Override
			protected IDataSet findNext(AtomicInteger context) {
				final int page = context.getAndIncrement();
				if (last != null)
					last.dispose(fastOrm.getContainer().getMemoryManager());

				IDataSet result = fastOrm.getJdbcTemplate().execute(new ConnectionCallback<IDataSet>() {
					@Override
					public IDataSet doInConnection(Connection connection) throws SQLException, DataAccessException {
						final OrmReadContext ormReadContext = new OrmReadContext(connection);
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

						class TruncateTempTables implements IMakerAndEntityDefnVisitor {
							@Override
							public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								maker.truncate(fastOrm, ormReadContext);
							}

							@Override
							public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								maker.truncate(fastOrm, ormReadContext, parent, child);

							}
						}
						class PopulateTempTables implements IMakerAndEntityDefnVisitor {
							@Override
							public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								maker.populate(fastOrm, ormReadContext, page);
							}

							@Override
							public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								maker.populate(fastOrm, ormReadContext, parent, child);

							}
						}

						class DrainTables implements IMakerAndEntityDefnFoldVisitor<IDrainedTableData, IDataSet> {

							@Override
							public IDrainedTableData acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
								return maker.drain(fastOrm, ormReadContext);
							}

							@Override
							public IDrainedTableData acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
								return maker.drain(fastOrm, ormReadContext, parent, child);
							}
						}
						ICallback<Long> total = new ICallback<Long>() {
							@Override
							public void process(Long t) throws Exception {
								fastOrm.getSqlLogger().total(t);
							}
						};
						if (page == 0)
							IEntityDefn.Utils.walk(fastOrm, new CreateTempTables());
						IEntityDefn.Utils.walkAndTime(fastOrm, new TruncateTempTables(), total);
						IEntityDefn.Utils.walkAndTime(fastOrm, new PopulateTempTables(), total);
						last = IEntityDefn.Utils.aggregateAndTime(fastOrm, new DrainTables(), new DataSetBuilder(), total);
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