package org.fastorm.sqlDialects;

import java.util.Set;

import junit.framework.TestCase;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.collections.Sets;
import org.fastorm.utilities.maps.Maps;
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

}
