package org.fastorm.defns;

import java.util.Map;

import junit.framework.TestCase;

import org.fastorm.constants.FastOrmKeys;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.collections.Files;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.utilities.tests.IIntegrationTest;
import org.fastorm.xmlToMap.IMapPrinter;
import org.fastorm.xmlToMap.MapToXmlAttributePrinter;
import org.fastorm.xmlToMap.MapToXmlElementPrinter;
import org.fastorm.xmlToMap.XmlToMapParser;
import org.springframework.core.io.ClassPathResource;

public class EntityDefnParserTest extends TestCase implements IIntegrationTest {

	public void testSampleFilesAllMeanSameThing() {
		checkFile("sample1.xml");
		checkFile("sample2.xml");
		checkFile("sample3.xml");
		checkFile("sample4.xml");
	}

	private void checkFile(String string) {
		String xml = Files.getTextFromClassPath(getClass(), string);
		XmlToMapParser<String, Object> parser = IEntityDefn.Utils.makeXmlParser();
		Object actual = parser.parse(xml);
		assertEquals(EntityDefnTestFixture.person, actual);
	}

	public void testMakersAreAllocated() {
		final TempTableMakerFactory factory = new TempTableMakerFactory();
		ClassPathResource resource = new ClassPathResource("sample1.xml", getClass());
		IEntityDefn entityDefn = IEntityDefn.Utils.parse(factory, resource);
		IEntityDefn.Utils.walk(entityDefn, new IEntityDefnParentChildVisitor() {
			@Override
			public void acceptPrimary(IEntityDefn primary) throws Exception {
				assertNull(primary.getMaker());
			}

			@Override
			public void acceptChild(IEntityDefn parent, IEntityDefn child) throws Exception {
				Map<String, String> parentParameters = parent.parameters();
				Map<String, String> childParameters = child.parameters();
				ISecondaryTempTableMaker maker = factory.findReaderMakerFor(parentParameters, childParameters);
				assertNotNull(child.getEntityName(), maker);
				ISecondaryTempTableMaker childMaker = child.getMaker();
				assertNotNull(child.getEntityName(), childMaker);
				assertEquals(maker.getClass(), childMaker.getClass());
			}
		});
	}

	public void testSmokeTestForDefn() {
		XmlToMapParser<String, Object> parser = IEntityDefn.Utils.makeXmlParser();
		Map<String, Object> expected = Maps.makeMap(//
				FastOrmKeys.tableName, "",//
				FastOrmKeys.tempTableName, "_temp",//
				FastOrmKeys.entityName, "",//
				FastOrmKeys.versionColumn, "",//
				FastOrmKeys.idColumn, "",//
				FastOrmKeys.useTemporaryTable, true,//
				FastOrmKeys.maxLinesPerBatch, "100",//
				FastOrmKeys.idType, "integer");
		checkXml(parser, expected, new MapToXmlElementPrinter<String, Object>("Entity"));
		checkXml(parser, expected, new MapToXmlAttributePrinter<String, Object>("Entity"));
	}

	private void checkXml(XmlToMapParser<String, Object> parser, Map<String, Object> expected, IMapPrinter<String, Object> printer) {
		String xml = printer.print(expected);
		Map<String, Object> defn = parser.parse(xml);
		assertEquals(expected, defn);
	}

}
