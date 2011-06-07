package org.fastorm.sqlDialects;

import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.collections.Sets;
import org.fastorm.utilities.maps.IListOfSimpleMapWithIndex;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.utilities.maps.SimpleMaps;
import org.fastorm.utilities.reflection.Fields;
import org.springframework.core.io.ClassPathResource;

public class SqlStringsTest extends TestCase {
	private final FastOrmOptions options = FastOrmOptions.usualBest();

	public void testGetFromTemplate() {
		SqlStrings sqlStrings = new SqlStrings(new ClassPathResource("Dummy.st", getClass()));
		assertEquals("got q1 and q2", sqlStrings.getFromTemplate(options, "template1", Maps.<String, Object> makeMap("p1", "q1", "p2", "q2")));
		assertEquals("got q1 and q2", sqlStrings.getFromTemplate(options, "template1", Maps.<String, Object> makeMap("p1", "q1"), "p2", "q2"));
		assertEquals("got q1 and q2", sqlStrings.getFromTemplate(options, "template1", Maps.<String, Object> makeMap(), "p1", "q1", "p2", "q2"));
	}

	public void testTemplateMatchsFastOrmTemplates() {
		SqlStrings sqlStrings = new SqlStrings(new ClassPathResource("MySql.st"));
		Set<String> expected = Sets.set(Fields.names(Fields.constants(FastOrmStringTemplates.class)));
		expected.addAll(FastOrmStringTemplates.internalTemplateNames());
		assertEquals(Lists.sort(expected), Lists.sort(sqlStrings.getTemplateNames()));
	}

	public void testUseTemporaryTablesInFastOrmOptionsOverridesParameters() {
		SqlStrings sqlStrings = new SqlStrings(new ClassPathResource("DummyWIthTempTables.st", getClass()));
		assertEquals("got q1 and q2 Temp:true", sqlStrings.getFromTemplate(options, "template1", Maps.<String, Object> makeMap("p1", "q1", "p2", "q2", "useTemporaryTable", true)));
		assertEquals("got q1 and q2 Temp:true", sqlStrings.getFromTemplate(options, "template1", Maps.<String, Object> makeMap("p1", "q1", "useTemporaryTable", true), "p2", "q2"));
		FastOrmOptions noTempTables = FastOrmOptions.withOutTempTables();
		assertEquals("got q1 and q2 Temp:", sqlStrings.getFromTemplate(noTempTables, "template1", Maps.<String, Object> makeMap("p1", "q1", "p2", "q2", "useTemporaryTable", true)));
		assertEquals("got q1 and q2 Temp:", sqlStrings.getFromTemplate(noTempTables, "template1", Maps.<String, Object> makeMap("p1", "q1", "useTemporaryTable", true), "p2", "q2"));
		assertEquals("got q1 and q2 Temp:", sqlStrings.getFromTemplate(noTempTables, "template1", Maps.<String, Object> makeMap(), "p1", "q1", "p2", "q2"));
	}

	@SuppressWarnings("unchecked")
	public void testBulkInsert() {
		ISimpleMapWithIndex<String, Object> map1 = SimpleMaps.makeMapWithIndex("a", 1, "b", 2);
		ISimpleMapWithIndex<String, Object> map2 = SimpleMaps.makeMapWithIndex("a", 3, "b", 4);
		IListOfSimpleMapWithIndex<String, Object> list = SimpleMaps.makeList(map1, map2);
		SqlStrings sqlStrings = new SqlStrings(new ClassPathResource("DummyWIthTempTables.st", getClass()));
		String sql = sqlStrings.buildInsert("t", list);
		assertEquals("insert into t(a,b) values ('1','2'),('3','4')", sql);
	}

	public void testBulkInsertWithColumns() {
		ISimpleMapWithIndex<String, Object> map1 = SimpleMaps.makeMapWithIndex("a", 1, "b", 2, "c", 3);
		ISimpleMapWithIndex<String, Object> map2 = SimpleMaps.makeMapWithIndex("a", 4, "b", 5, "c", 6);
		@SuppressWarnings("unchecked")
		IListOfSimpleMapWithIndex<String, Object> list = SimpleMaps.makeList(map1, map2);
		SqlStrings sqlStrings = new SqlStrings(new ClassPathResource("DummyWIthTempTables.st", getClass()));
		assertEquals("insert into t(a) values ('1'),('4')", sqlStrings.buildInsert("t", list, Arrays.asList(0)));
		assertEquals("insert into t(b) values ('2'),('5')", sqlStrings.buildInsert("t", list, Arrays.asList(1)));
		assertEquals("insert into t(c) values ('3'),('6')", sqlStrings.buildInsert("t", list, Arrays.asList(2)));
		assertEquals("insert into t(a,b) values ('1','2'),('4','5')", sqlStrings.buildInsert("t", list, Arrays.asList(0, 1)));
		assertEquals("insert into t(b,c) values ('2','3'),('5','6')", sqlStrings.buildInsert("t", list, Arrays.asList(1, 2)));
		assertEquals("insert into t(a,c) values ('1','3'),('4','6')", sqlStrings.buildInsert("t", list, Arrays.asList(0, 2)));
		assertEquals("insert into t(a,b,c) values ('1','2','3'),('4','5','6')", sqlStrings.buildInsert("t", list, Arrays.asList(0, 1, 2)));
	}
}
