package org.fastorm.temp.impl;

import static org.fastorm.constants.FastOrmTestValues.primaryIdColumn;
import static org.fastorm.constants.FastOrmTestValues.primaryIdType;
import static org.fastorm.constants.FastOrmTestValues.primaryTableName;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.mutate.IMutableItem;
import org.fastorm.mutate.IMutator;
import org.fastorm.utilities.annotations.IntegrationTest;
import org.fastorm.utilities.callbacks.ICallback;

@IntegrationTest
public class MutatingTempTableMakerTest extends AbstractTempTableMakerTest {

	public void testReadModifyWriteOfItems() {
		populate();
		final IEntityDefn entityDefn = fastOrm5.getEntityDefn();
		IMutator<Object> mutate = fastOrm5.makeMutator();
		mutate.readModifyWrite(new AllEntitiesTempTableMaker(), new ICallback<IMutableItem>() {
			private int i;

			@Override
			public void process(IMutableItem old) throws Exception {
				assertEquals("data_" + i, old.get("data"));
				old.put("data", "newData_" + i);
				assertEquals("newData_" + i, old.get("data"));
				i++;
			}
		});
		checkName(entityDefn, 1, "newData_1");
		checkName(entityDefn, 2, "data_1");
		checkName(entityDefn, 3, "data_2");
	}

	private void populate() {
		emptyDatabase();
		sqlHelper.create(primaryTableName, primaryIdColumn, primaryIdType, "data", "varchar(20)");
		sqlHelper.insert(primaryTableName, 3, primaryIdColumn, "{1}", "data", "''{0}_{1}''");
	}

	private void checkName(final IEntityDefn entityDefn, Object id, String expectedName) {
		String tableName = entityDefn.getTableName();
		String actualName = jdbcTemplate.queryForObject("select data from " + tableName + " where " + entityDefn.getIdColumn() + " =" + id, String.class);
		assertEquals(expectedName, actualName);
	}
}
