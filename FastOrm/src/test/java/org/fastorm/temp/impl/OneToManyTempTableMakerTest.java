package org.fastorm.temp.impl;

import static org.fastorm.constants.FastOrmKeys.childLink;
import static org.fastorm.constants.FastOrmKeys.idColumn;
import static org.fastorm.constants.FastOrmKeys.idType;
import static org.fastorm.constants.FastOrmKeys.tableName;
import static org.fastorm.constants.FastOrmKeys.tempTableName;
import static org.fastorm.constants.FastOrmTestValues.childIdColumn;
import static org.fastorm.constants.FastOrmTestValues.childIdType;
import static org.fastorm.constants.FastOrmTestValues.childLinkColumn;
import static org.fastorm.constants.FastOrmTestValues.childTableName;
import static org.fastorm.constants.FastOrmTestValues.childTempTableName;
import static org.fastorm.constants.FastOrmTestValues.primaryIdColumn;
import static org.fastorm.constants.FastOrmTestValues.primaryIdType;
import static org.fastorm.constants.FastOrmTestValues.primaryTableName;

import java.util.Arrays;
import java.util.Collections;

import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.impl.EntityDefn;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.sql.SysOutSqlLogger;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.Maps;
import org.fastorm.utilities.Sets;

public class OneToManyTempTableMakerTest extends AbstractTempTableMakerTest {
	private final IEntityDefn child = new EntityDefn(new OneToMany(), Maps.<String, String> makeMap(//
			tableName, childTableName,//
			tempTableName, childTempTableName,//
			idColumn, childIdColumn,//
			idType, childIdType,//
			childLink, childLinkColumn), Collections.<IEntityDefn> emptyList());
	private OneToMany maker;
	private IEntityDefn primary;
	private AllEntitiesTempTableMaker primaryMaker;

	public void testCreateTempTable() {
		emptyDatabase();
		execute(new ICallback<OrmReadContext>() {
			@Override
			public void process(OrmReadContext context) throws Exception {
				maker.create(fastOrm, context, primary, child);
			}
		});
		assertEquals(Arrays.asList(childTempTableName), sqlHelper.tables());
		assertEquals(Maps.makeMap(childIdColumn, "INT", childLinkColumn, "INT"), sqlHelper.columns(childTempTableName));
		assertEquals(0, sqlHelper.tableSize(childTempTableName));
	}

	public void testPopulate() {
		makePrimaryAndChildTables();
		final IFastOrmContainer fastOrm5 = fastOrm.withDataSize(5).withSqlLogger(new SysOutSqlLogger()).getContainer();
		sqlHelper.insert(childTableName, childIdColumn, 1, childLinkColumn, 1);
		sqlHelper.insert(childTableName, childIdColumn, 2, childLinkColumn, 1);
		sqlHelper.insert(childTableName, childIdColumn, 3, childLinkColumn, 1);
		sqlHelper.insert(childTableName, childIdColumn, 4, childLinkColumn, 5);
		populateSecondary(fastOrm5, 0);
		assertEquals(3, fastOrm5.getJdbcTemplate().queryForInt("select count(*) from " + childTempTableName));
		truncatePrimaryAndSecondary();
		populateSecondary(fastOrm5, 1);
		assertEquals(1, fastOrm5.getJdbcTemplate().queryForInt("select count(*) from " + childTempTableName));
	}

	private void truncatePrimaryAndSecondary() {
		execute(new ICallback<OrmReadContext>() {
			@Override
			public void process(OrmReadContext context) throws Exception {
				primaryMaker.truncate(fastOrm, context);
				maker.truncate(fastOrm, context, primary, child);
			}
		});
	}

	private void populateSecondary(final IFastOrmContainer fastOrm5, final int page) {
		execute(new ICallback<OrmReadContext>() {
			@Override
			public void process(OrmReadContext context) throws Exception {
				primaryMaker.populate(fastOrm5, context, page);
				maker.populate(fastOrm5, context, primary, child);
			}
		});
	}

	private void makePrimaryAndChildTables() {
		emptyDatabase();
		execute(new ICallback<OrmReadContext>() {
			@Override
			public void process(OrmReadContext context) throws Exception {
				primaryMaker.create(fastOrm5, context);
				maker.create(fastOrm5, context, primary, child);

			}
		});
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 20, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
		sqlHelper.create(childTableName, childIdColumn, childIdType, childLinkColumn, "integer", "childData", "varchar(20)");
	}

	@SuppressWarnings("unchecked")
	public void testDrain() {
		emptyDatabase();
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 20, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
		sqlHelper.create(childTableName, childIdColumn, childIdType, childLinkColumn, "integer", "childData", "varchar(20)");
		sqlHelper.insertWithOffset(childTableName, 3, 0, childIdColumn, "{1}", childLinkColumn, 5, "childData", "''{0}_{1}''");
		sqlHelper.insertWithOffset(childTableName, 1, 3, childIdColumn, "{1}", childLinkColumn, 6, "childData", "''{0}_{1}''");
		sqlHelper.insertWithOffset(childTableName, 2, 4, childIdColumn, "{1}", childLinkColumn, 8, "childData", "''{0}_{1}''");
		final IFastOrmContainer fastOrm5 = fastOrm.withDataSize(5).getContainer();
		final AllEntitiesTempTableMaker primaryMaker = new AllEntitiesTempTableMaker();
		IDrainedTableData table = query(new IFunction1<OrmReadContext, IDrainedTableData>() {
			@Override
			public IDrainedTableData apply(OrmReadContext context) throws Exception {
				primaryMaker.create(fastOrm5, context);
				primaryMaker.populate(fastOrm5, context, 1);
				maker.create(fastOrm5, context, primary, child);
				maker.populate(fastOrm5, context, primary, child);
				IDrainedTableData table = maker.drain(fastOrm5, context, primary, child);
				return table;
			}
		});
		assertEquals(6, table.size());
		assertEquals(child, table.getEntityDefn());
		Sets.assertMatches(Arrays.asList(childIdColumn, childLinkColumn, "childData", childIdColumn, childLinkColumn), table.getColumnNames());
		Sets.assertMatches(Arrays.asList(0, 5, "childData_0", 0, 5), Arrays.asList(table.getLine(0)));
		Sets.assertMatches(Arrays.asList(1, 5, "childData_1", 1, 5), Arrays.asList(table.getLine(1)));
		Sets.assertMatches(Arrays.asList(2, 5, "childData_2", 2, 5), Arrays.asList(table.getLine(2)));
		Sets.assertMatches(Arrays.asList(3, 6, "childData_3", 3, 6), Arrays.asList(table.getLine(3)));
		Sets.assertMatches(Arrays.asList(4, 8, "childData_4", 4, 8), Arrays.asList(table.getLine(4)));
		Sets.assertMatches(Arrays.asList(5, 8, "childData_5", 5, 8), Arrays.asList(table.getLine(5)));
		assertEquals(2, table.getIdColumnIndex());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		primaryMaker = new AllEntitiesTempTableMaker();
		maker = new OneToMany();
		primary = fastOrm.getEntityDefn();
	}
}
