package org.fastorm.xmlToMap;

import java.util.Map;

import junit.framework.TestCase;

import org.fastorm.utilities.functions.Functions;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.xmlItemStrategies.IItemStrategy;
import org.fastorm.xmlItemStrategies.SimpleStringItemStrategy;
import org.fastorm.xmlItemStrategies.SimpleStringUseElementNameItemStrategy;

public class XmlToMapParserWithDefaultsTest extends TestCase {

	@SuppressWarnings("rawtypes")
	private final IUnrecognisedStrategy noUnrecognisedAllowed = new NothingUnrecognised();

	private final IItemStrategy<String, Object> eltName = new SimpleStringUseElementNameItemStrategy();
	private final IItemStrategy<String, Object> default7 = new SimpleStringItemStrategy("7", false);
	private final IItemStrategy<String, Object> default9 = new SimpleStringItemStrategy("9", false);
	private final IItemStrategy<String, Object> default11 = new SimpleStringItemStrategy("11", false);
	private final IItemStrategy<String, Object> default13 = new SimpleStringItemStrategy("13", false);

	private final Map<String, IStrategyNode<String, Object>> empty = Maps.newMap();
	private final Map<String, IStrategyNode<String, Object>> hasA = Maps.makeMap("a", default7);

	private final Map<String, IStrategyNode<String, Object>> hasAB = Maps.makeMap("a", default7, "b", default9);
	private final Map<String, IStrategyNode<String, Object>> hasABAndEltName = Maps.makeMap("a", default7, "b", default9, "elt", eltName);

	@SuppressWarnings("unchecked")
	private final XmlToMapStrategy<String, Object> cStrategy = new XmlToMapStrategy<String, Object>(//
			hasABAndEltName, //
			noUnrecognisedAllowed,//
			Functions.<Map<String, Object>, Object> identity(), Functions.<String, String> identity());
	private final Map<String, IStrategyNode<String, Object>> hasABCasMap = Maps.makeMap("a", default11, "b", default13, "c", cStrategy);

	public void testOneLayer() {
		Map<String, Object> blankMap = Maps.<String, Object> newMap();
		checkParser(empty, blankMap, blankMap);
		checkParser(hasA, Maps.<String, Object> makeMap("a", "7"), blankMap);
		checkParser(hasA, Maps.<String, Object> makeMap("a", "1"), Maps.<String, Object> makeMap("a", "1"));

		checkParser(hasAB, Maps.<String, Object> makeMap("a", "7", "b", "9"), blankMap);
		checkParser(hasAB, Maps.<String, Object> makeMap("a", "1", "b", "9"), Maps.<String, Object> makeMap("a", "1"));
		checkParser(hasAB, Maps.<String, Object> makeMap("a", "1", "b", "2"), Maps.<String, Object> makeMap("a", "1", "b", "2"));
	}

	public void testTwoLayer() {
		Map<String, Object> blankMap = Maps.<String, Object> newMap();
		checkParser(hasABCasMap, //
				Maps.<String, Object> makeMap("a", "11", "b", "13", "c", Maps.<String, Object> makeMap("a", "7", "b", "9", "elt", "c")),//
				Maps.<String, Object> makeMap("c", blankMap));
		checkParser(hasABCasMap, //
				Maps.<String, Object> makeMap("a", "11", "b", "13", "c", Maps.<String, Object> makeMap("a", "7", "b", "9", "elt", "someName")),//
				Maps.<String, Object> makeMap("c", Maps.makeMap("elt", "someName")));
	}

	private <K, V> void checkParser(Map<String, IStrategyNode<K, V>> idToStrategyMap, Map<K, V> expected, Map<K, V> xmlMap) {
		checkParser(new MapToXmlAttributePrinter<K, V>("root"), idToStrategyMap, expected, xmlMap);
		checkParser(new MapToXmlElementPrinter<K, V>("root"), idToStrategyMap, expected, xmlMap);
	}

	@SuppressWarnings("unchecked")
	private <K, V> void checkParser(IMapPrinter<K, V> printer, Map<String, IStrategyNode<K, V>> idToStrategyMap, Map<K, V> expected, Map<K, V> xmlMap) {
		XmlToMapStrategy<K, V> strategy = new XmlToMapStrategy<K, V>(//
				idToStrategyMap, //
				noUnrecognisedAllowed,//
				Functions.<Map<K, V>, V> identity(), //
				Functions.<String, K> identity());
		String xml = printer.print(xmlMap);
		XmlToMapParser<K, V> parser = new XmlToMapParser<K, V>(strategy);
		Map<K, V> actual = parser.parse(xml);
		assertEquals(expected, actual);

	}

}
