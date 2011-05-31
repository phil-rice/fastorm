package org.fastorm.temp.impl;

import static org.fastorm.constants.FastOrmTestValues.primaryIdColumn;
import static org.fastorm.constants.FastOrmTestValues.primaryIdType;
import static org.fastorm.constants.FastOrmTestValues.primaryTableName;
import static org.fastorm.constants.FastOrmTestValues.primaryTempTableName;

import java.util.Arrays;

import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.sql.SysOutSqlLogger;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.collections.Sets;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;

public class AllEntitiesTempTableMakerTest extends AbstractTempTableMakerTest {
	protected AllEntitiesTempTableMaker maker;

	public void testCreateTempTable() {
		emptyDatabase();
		execute(new ICallback<OrmReadContext>() {
			@Override
			public void process(OrmReadContext context) throws Exception {
				maker.create(fastOrm, context);
			}
		});
		assertEquals(Arrays.asList(primaryTempTableName), sqlHelper.tables());
		assertEquals(Maps.makeMap(primaryIdColumn, "INT"), sqlHelper.columns(primaryTempTableName));
		assertEquals(0, sqlHelper.tableSize(primaryTempTableName));
	}

	public void testPopulate() {
		emptyDatabase();
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 100, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
		sqlHelper.assertTableMatches(primaryTableName, 100, 0, primaryIdColumn, "{1}", "data", "{0}_{1}");
		execute(new ICallback<OrmReadContext>() {
			@Override
			public void process(OrmReadContext context) throws Exception {
				fastOrm5 = fastOrm5.withSqlLogger(new SysOutSqlLogger()).getContainer();
				maker.create(fastOrm5, context);
				int popCount0 = maker.populate(fastOrm5, context, 0);
				assertEquals(5, popCount0);
				sqlHelper.assertTableMatches(primaryTempTableName, 5, 0, primaryIdColumn, "{1}");
				maker.truncate(fastOrm, context);
				int popCount1 = maker.populate(fastOrm5, context, 1);
				assertEquals(5, popCount1);
				sqlHelper.assertTableMatches(primaryTempTableName, 5, 5, primaryIdColumn, "{1}");
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void testDrain() {

		emptyDatabase();
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 10, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
		IDrainedTableData table = query(new IFunction1<OrmReadContext, IDrainedTableData>() {
			@Override
			public IDrainedTableData apply(OrmReadContext from) throws Exception {
				maker.create(fastOrm5, from);
				maker.populate(fastOrm5, from, 0);
				IDrainedTableData table = maker.drain(fastOrm5, from);
				return table;
			}
		});
		assertEquals(5, table.size());
		assertTrue(table.getIdColumnIndex() != -1);
		assertEquals(fastOrm5.getEntityDefn(), table.getEntityDefn());
		assertEquals(Sets.makeSet(primaryIdColumn, "data"), Sets.set(table.getColumnNames()));
		Sets.assertMatches(Arrays.asList(0, "data_0", 0), Arrays.asList(table.getLine(0)));
		Sets.assertMatches(Arrays.asList(1, "data_1", 1), Arrays.asList(table.getLine(1)));
		Sets.assertMatches(Arrays.asList(2, "data_2", 2), Arrays.asList(table.getLine(2)));
		Sets.assertMatches(Arrays.asList(3, "data_3", 3), Arrays.asList(table.getLine(3)));
		Sets.assertMatches(Arrays.asList(4, "data_4", 4), Arrays.asList(table.getLine(4)));
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		maker = new AllEntitiesTempTableMaker();
	}
}
