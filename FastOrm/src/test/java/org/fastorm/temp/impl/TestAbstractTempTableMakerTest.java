package org.fastorm.temp.impl;

import java.util.Arrays;
import java.util.Collections;

public class TestAbstractTempTableMakerTest extends AbstractTempTableMakerTest {

	public void testEmptyDatabaseCreateTableAndTables() {
		emptyDatabase();
		assertEquals(Collections.EMPTY_LIST, sqlHelper.tables());
		sqlHelper.create("sometable", "id", "integer");
		assertEquals(Arrays.asList("sometable"), sqlHelper.tables());
		emptyDatabase();
		assertEquals(Collections.EMPTY_LIST, sqlHelper.tables());
	}

}
