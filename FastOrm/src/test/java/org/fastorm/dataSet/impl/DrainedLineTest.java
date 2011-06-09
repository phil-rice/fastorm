package org.fastorm.dataSet.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.fastorm.api.IJobDetails;
import org.fastorm.constants.FastOrmMessages;
import org.fastorm.defns.EntityDefnTestFixture;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.memory.MemoryManager;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.SimpleMaps;
import org.fastorm.utilities.mocks.ResultSetMock;
import org.fastorm.utilities.tests.Tests;

public class DrainedLineTest extends TestCase {
	private DrainedLineCommonData commonData;
	private IMemoryManager memoryManager;
	private IEntityDefn entityDefn;
	private ResultSetMock resultSet;
	private final DrainedLine line0 = new DrainedLine();
	private final DrainedLine line1 = new DrainedLine();
	private GetDrainedTableForEntityDefnMock getter;
	private DrainedTableData addressData;
	private DrainedTableData personData;

	public void testGetOfColumns() throws SQLException {
		assertEquals(1, line0.get("id"));
		assertEquals(3, line1.get("id"));

		assertEquals("a", line0.get("data"));
		assertEquals("c", line1.get("data"));
	}

	public void testGetbyIndexOfColumns() {
		assertEquals(1, line0.getByIndex(0));
		assertEquals(3, line1.getByIndex(0));

		assertEquals("a", line0.getByIndex(1));
		assertEquals("c", line1.getByIndex(1));
	}

	@SuppressWarnings("unchecked")
	public void testGettingChildObjects() {
		// This requires the getters to be set up
		personData.setData(memoryManager, EntityDefnTestFixture.person, getter, resultSet.resetMock());
		addressData.setData(memoryManager, EntityDefnTestFixture.address, getter, new ResultSetMock(Arrays.asList("id", "a_person", "data"), Arrays.asList(0, 1, "P0C0"), Arrays.asList(1, 1, "P0C1"), Arrays.asList(2, 3, "P1C0")));
		checkChild(line0.get("address"), line0.getByIndex(2),//
				SimpleMaps.<String, Object> makeMap("id", 0, "a_person", 1, "data", "P0C0"), //
				SimpleMaps.<String, Object> makeMap("id", 1, "a_person", 1, "data", "P0C1"));
		checkChild(line1.get("address"), line1.getByIndex(2),//
				SimpleMaps.<String, Object> makeMap("id", 2, "a_person", 3, "data", "P1C0"));
	}

	@SuppressWarnings("unchecked")
	private void checkChild(Object object, Object objectByIndex, ISimpleMap<String, Object>... expected) {
		assertSame(object, objectByIndex);
		assertEquals(SimpleMaps.toListOfMaps(Arrays.asList(expected)), SimpleMaps.toListOfMaps((List<ISimpleMap<String, Object>>) object));
	}

	public void testClean() {
		checkGetCausesExceptionAfterClean(new Runnable() {
			public void run() {
				line0.get("id");
			}
		});
		checkGetCausesExceptionAfterClean(new Runnable() {
			public void run() {
				line0.getByIndex(0);
			}
		});
	}

	private void checkGetCausesExceptionAfterClean(Runnable runnable) {
		line0.clean();
		IllegalStateException e = Tests.assertThrows(IllegalStateException.class, runnable);
		assertEquals(FastOrmMessages.cannotAccessBeforeItHasBeenSetup, e.getMessage());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resultSet = new ResultSetMock(Arrays.asList("id", "data"), Arrays.asList(1, "a"), Arrays.asList(3, "c"));
		memoryManager = new MemoryManager().withJobDetails(IJobDetails.Utils.withTempTablesForTests(false));
		entityDefn = EntityDefnTestFixture.person;
		commonData = new DrainedLineCommonData(memoryManager, entityDefn);
		addressData = new DrainedTableData(100);
		personData = new DrainedTableData(100);
		getter = new GetDrainedTableForEntityDefnMock(//
				EntityDefnTestFixture.person, personData,//
				EntityDefnTestFixture.address, addressData,//
				EntityDefnTestFixture.telephone, new DrainedTableData(100),//
				EntityDefnTestFixture.employer, new DrainedTableData(100));
		commonData.setData(getter, resultSet.getMetaData());
		resultSet.next();
		line0.setValuesFrom(commonData, resultSet, 0);
		resultSet.next();
		line1.setValuesFrom(commonData, resultSet, 1);
	}
}
