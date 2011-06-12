package org.fastorm.dataSet.impl;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.fastorm.api.IJobDetails;
import org.fastorm.defns.EntityDefnTestFixture;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.MemoryManager;
import org.fastorm.utilities.mocks.ResultSetMetaDataMock;

public class DrainedLineCommonDataTest extends TestCase {
	private DrainedLineCommonData commonData;
	private MemoryManager memoryManager;
	private IEntityDefn entityDefn;

	public void testAccessors() {
		assertSame(memoryManager, commonData.getMemoryManager());
		assertEquals(entityDefn.getChildren().size(), commonData.getChildCount());
		assertNull(commonData.getGetter());
		assertEquals(0, commonData.getColumnCount());
		assertEquals(-1, commonData.getIdColumnIndex());
		assertNull(commonData.getKeys());
	}

	public void testGetGetter() throws SQLException {
		ResultSetMetaData metaData = new ResultSetMetaDataMock(Arrays.asList("id", "a"));
		GetDrainedTableForEntityDefnMock getter = new GetDrainedTableForEntityDefnMock();
		commonData.setData(getter, metaData);
		assertEquals(getter, commonData.getGetter());
		assertEquals(0, commonData.getIdColumnIndex());
		assertEquals(2, commonData.getColumnCount());
		assertEquals(Arrays.asList("id", "a", "address", "employer", "telephone"), commonData.getKeys());
	}

	public void testGetterWithDifferentOrder() throws SQLException {
		ResultSetMetaData metaData = new ResultSetMetaDataMock(Arrays.asList("a", "id"));
		GetDrainedTableForEntityDefnMock getter = new GetDrainedTableForEntityDefnMock();
		commonData.setData(getter, metaData);
		assertEquals(getter, commonData.getGetter());
		assertEquals(1, commonData.getIdColumnIndex());
		assertEquals(2, commonData.getColumnCount());
		assertEquals(Arrays.asList("a", "id", "address", "employer", "telephone"), commonData.getKeys());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		memoryManager = new MemoryManager();
		entityDefn = EntityDefnTestFixture.person;
		commonData = new DrainedLineCommonData(memoryManager, IJobDetails.Utils.allEntities(entityDefn, 100), entityDefn);
	}

}
