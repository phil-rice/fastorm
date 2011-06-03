package org.fastorm.temp.impl;

import static org.fastorm.constants.FastOrmTestValues.primaryIdColumn;
import static org.fastorm.constants.FastOrmTestValues.primaryIdType;
import static org.fastorm.constants.FastOrmTestValues.primaryTableName;
import static org.fastorm.constants.FastOrmTestValues.primaryTempTableName;

import java.util.Arrays;

import org.fastorm.constants.FastOrmTestValues;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.sql.SysOutSqlLogger;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.utilities.maps.SimpleMaps;

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

	public void testDrain() {
		emptyDatabase();
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 10, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
		IDrainedTableData table = query(new IFunction1<OrmReadContext, IDrainedTableData>() {
			@Override
			public IDrainedTableData apply(OrmReadContext from) throws Exception {
				maker.drop(fastOrm5, from);// this is for debugging so that you can drop to stack frame here
				maker.create(fastOrm5, from);
				maker.populate(fastOrm5, from, 0);
				maker.drain(fastOrm5, from);
				IDrainedTableData iDrainedTableData = from.get(fastOrm5.getEntityDefn());
				return iDrainedTableData;
			}
		});
		assertEquals(5, table.size());
		assertTrue(table.getIdColumnIndex() != -1);
		assertEquals(fastOrm5.getEntityDefn(), table.getEntityDefn());
		SimpleMaps.assertEquals(table.getMap(0), FastOrmTestValues.primaryIdColumn, 0, "data", "data_0");
		SimpleMaps.assertEquals(table.getMap(1), FastOrmTestValues.primaryIdColumn, 1, "data", "data_1");
		SimpleMaps.assertEquals(table.getMap(2), FastOrmTestValues.primaryIdColumn, 2, "data", "data_2");
		SimpleMaps.assertEquals(table.getMap(3), FastOrmTestValues.primaryIdColumn, 3, "data", "data_3");
		SimpleMaps.assertEquals(table.getMap(4), FastOrmTestValues.primaryIdColumn, 4, "data", "data_4");
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		maker = new AllEntitiesTempTableMaker();
	}
}
