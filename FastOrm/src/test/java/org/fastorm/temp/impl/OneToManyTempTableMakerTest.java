package org.fastorm.temp.impl;

import static org.fastorm.constants.FastOrmKeys.childLink;
import static org.fastorm.constants.FastOrmKeys.entityName;
import static org.fastorm.constants.FastOrmKeys.idColumn;
import static org.fastorm.constants.FastOrmKeys.idType;
import static org.fastorm.constants.FastOrmKeys.maxLinesPerBatch;
import static org.fastorm.constants.FastOrmKeys.tableName;
import static org.fastorm.constants.FastOrmKeys.tempTableName;
import static org.fastorm.constants.FastOrmTestValues.childEntityName;
import static org.fastorm.constants.FastOrmTestValues.childIdColumn;
import static org.fastorm.constants.FastOrmTestValues.childIdType;
import static org.fastorm.constants.FastOrmTestValues.childLinkColumn;
import static org.fastorm.constants.FastOrmTestValues.childMaxLinesPerBatch;
import static org.fastorm.constants.FastOrmTestValues.childTableName;
import static org.fastorm.constants.FastOrmTestValues.childTempTableName;
import static org.fastorm.constants.FastOrmTestValues.primaryIdColumn;
import static org.fastorm.constants.FastOrmTestValues.primaryIdType;
import static org.fastorm.constants.FastOrmTestValues.primaryTableName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.IJobOptimisations;
import org.fastorm.constants.FastOrmTestValues;
import org.fastorm.context.IContext;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.impl.EntityDefn;
import org.fastorm.sql.SysOutSqlLogger;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.utilities.maps.SimpleMaps;

public class OneToManyTempTableMakerTest extends AbstractTempTableMakerTest {
	final IEntityDefn child = new EntityDefn(new OneToMany(IJobOptimisations.Utils.withNoOptimisation()), Maps.<String, String> makeMap(//
			entityName, childEntityName,//
			tableName, childTableName,//
			tempTableName, childTempTableName,//
			idColumn, childIdColumn,//
			idType, childIdType,//
			maxLinesPerBatch, childMaxLinesPerBatch,//
			childLink, childLinkColumn), Collections.<IEntityDefn> emptyList());
	OneToMany maker;
	IEntityDefn primary;
	private AllEntitiesTempTableMaker primaryMaker;

	public void testCreateTempTable() {
		emptyDatabase();
		execute(new ICallback<IContext>() {
			@Override
			public void process(IContext context) throws Exception {
				maker.create(context, primary, child);
			}
		});
		assertEquals(Arrays.asList(childTempTableName), sqlHelper.tables());
		assertEquals(Maps.makeMap(childIdColumn, "INT", childLinkColumn, "INT"), sqlHelper.columns(childTempTableName));
		assertEquals(0, sqlHelper.tableSize(childTempTableName));
	}

	public void testPopulate() {
		makePrimaryAndChildTables();
		final IFastOrmContainer fastOrm5 = fastOrm.withBatchSize(5).withSqlLogger(new SysOutSqlLogger()).getContainer();
		sqlHelper.insert(childTableName, childIdColumn, 1, childLinkColumn, 1);
		sqlHelper.insert(childTableName, childIdColumn, 2, childLinkColumn, 1);
		sqlHelper.insert(childTableName, childIdColumn, 3, childLinkColumn, 1);
		sqlHelper.insert(childTableName, childIdColumn, 4, childLinkColumn, 5);
		populateSecondary(fastOrm5, 0);
		assertEquals(3, jdbcTemplate.queryForInt("select count(*) from " + childTempTableName));
		truncatePrimaryAndSecondary();
		populateSecondary(fastOrm5, 1);
		assertEquals(1, jdbcTemplate.queryForInt("select count(*) from " + childTempTableName));
	}

	private void truncatePrimaryAndSecondary() {
		execute(new ICallback<IContext>() {
			@Override
			public void process(IContext context) throws Exception {
				primaryMaker.truncate(context);
				maker.truncate(context, primary, child);
			}
		});
	}

	private void populateSecondary(final IFastOrmContainer fastOrm5, final int page) {
		execute(new ICallback<IContext>() {
			@Override
			public void process(IContext context) throws Exception {
				primaryMaker.populate(context, page);
				maker.populate(context, primary, child);
			}
		});
	}

	private void makePrimaryAndChildTables() {
		emptyDatabase();
		execute(new ICallback<IContext>() {
			@Override
			public void process(IContext context) throws Exception {
				primaryMaker.create(context);
				maker.create(context, primary, child);

			}
		});
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 20, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
		sqlHelper.create(childTableName, childIdColumn, childIdType, childLinkColumn, "integer", "childData", "varchar(20)");
	}

	public void testDrain() {
		populateDataBase();
		final IFastOrmContainer fastOrm5 = fastOrm.withBatchSize(5).getContainer();
		IGetDrainedTableForEntityDefn getter = getTheTables(fastOrm5, maker, child, 1);

		IDrainedTableData table = getter.get(child);
		assertEquals(6, table.size());
		assertEquals(child, table.getEntityDefn());
		SimpleMaps.assertEquals(table.get(0), "childidcolumn", 0, "childData", "childData_0", "childLinkValue", 5);
		SimpleMaps.assertEquals(table.get(1), "childidcolumn", 1, "childData", "childData_1", "childLinkValue", 5);
		SimpleMaps.assertEquals(table.get(2), "childidcolumn", 2, "childData", "childData_2", "childLinkValue", 5);
		SimpleMaps.assertEquals(table.get(3), "childidcolumn", 3, "childData", "childData_3", "childLinkValue", 6);
		SimpleMaps.assertEquals(table.get(4), "childidcolumn", 4, "childData", "childData_4", "childLinkValue", 8);
		SimpleMaps.assertEquals(table.get(5), "childidcolumn", 5, "childData", "childData_5", "childLinkValue", 8);
		assertEquals(0, table.getIdColumnIndex());
	}

	public void testGetFromPrimaryIncludesChild() {
		final IFastOrmContainer fastOrm5 = fastOrm.withBatchSize(5).getContainer();
		IGetDrainedTableForEntityDefn getter = getTheTables(fastOrm5, maker, child, 1);
		IDrainedTableData table = getter.get(primary);
		IDrainedTableData childTable = getter.get(child);
		assertEquals(5, table.size());
		assertEquals(6, childTable.size());
		assertTrue(table.getIdColumnIndex() != -1);
		assertEquals(fastOrm5.getEntityDefn(), table.getEntityDefn());
		SimpleMaps.assertEquals(table.get(0), FastOrmTestValues.primaryIdColumn, 5, "data", "data_5", childEntityName, expectedChildren(childTable, 0, 1, 2));
		SimpleMaps.assertEquals(table.get(1), FastOrmTestValues.primaryIdColumn, 6, "data", "data_6", childEntityName, expectedChildren(childTable, 3));
		SimpleMaps.assertEquals(table.get(2), FastOrmTestValues.primaryIdColumn, 7, "data", "data_7", childEntityName, expectedChildren(childTable));
		SimpleMaps.assertEquals(table.get(3), FastOrmTestValues.primaryIdColumn, 8, "data", "data_8", childEntityName, expectedChildren(childTable, 4, 5));
		SimpleMaps.assertEquals(table.get(4), FastOrmTestValues.primaryIdColumn, 9, "data", "data_9", childEntityName, expectedChildren(childTable));
	}

	private List<ISimpleMapWithIndex<String, Object>> expectedChildren(IDrainedTableData childTable, int... indices) {
		List<ISimpleMapWithIndex<String, Object>> result = Lists.newList();
		for (int i : indices)
			result.add(childTable.get(i));
		return result;
	}

	private void populateDataBase() {
		emptyDatabase();
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 20, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
		sqlHelper.create(childTableName, childIdColumn, childIdType, childLinkColumn, "integer", "childData", "varchar(20)");
		sqlHelper.insertWithOffset(childTableName, 3, 0, childIdColumn, "{1}", childLinkColumn, 5, "childData", "''{0}_{1}''");
		sqlHelper.insertWithOffset(childTableName, 1, 3, childIdColumn, "{1}", childLinkColumn, 6, "childData", "''{0}_{1}''");
		sqlHelper.insertWithOffset(childTableName, 2, 4, childIdColumn, "{1}", childLinkColumn, 8, "childData", "''{0}_{1}''");
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		primaryMaker = new AllEntitiesTempTableMaker();
		maker = new OneToMany(IJobOptimisations.Utils.withNoOptimisation());
		primary = fastOrm.getEntityDefn();
	}

	@Override
	protected List<IEntityDefn> makeChildEntities() {
		return Arrays.asList(child);
	}
}
