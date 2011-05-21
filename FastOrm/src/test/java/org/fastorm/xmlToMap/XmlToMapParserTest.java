package org.fastorm.xmlToMap;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;

import junit.framework.TestCase;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.utilities.Functions;
import org.fastorm.utilities.Maps;
import org.fastorm.xmlItemStrategies.IItemStrategy;
import org.fastorm.xmlItemStrategies.SimpleStringItemStrategy;

public class XmlToMapParserTest extends TestCase {
	@SuppressWarnings("rawtypes")
	private IUnrecognisedStrategy noUnrecognisedAllowed = new NothingUnrecognised();

	private final IItemStrategy<String, Object> stringStrategy = new SimpleStringItemStrategy();
	private final IItemStrategy<String, Object> mandatoryStrategy = new SimpleStringItemStrategy(null, true);

	private final Map<String, IStrategyNode<String, Object>> empty = Maps.newMap();
	private final Map<String, IStrategyNode<String, Object>> hasA = Maps.makeMap("a", stringStrategy);
	private final Map<String, IStrategyNode<String, Object>> hasAB = Maps.makeMap("a", stringStrategy, "b", stringStrategy);
	private final Map<String, IStrategyNode<String, Object>> hasABMandatory = Maps.makeMap("a", mandatoryStrategy, "b", mandatoryStrategy);

	@SuppressWarnings("unchecked")
	private final XmlToMapStrategy<String, Object> cStrategy = new XmlToMapStrategy<String, Object>(hasAB, noUnrecognisedAllowed, Functions.<Map<String, Object>, Object> identity(), Functions.<String, String> identity());
	private final Map<String, IStrategyNode<String, Object>> hasABCasMap = Maps.makeMap("a", stringStrategy, "b", stringStrategy, "c", cStrategy);

	public void testOneLayer() {
		checkParser(empty);
		checkParser(hasA, "a", "1");
		checkParser(hasAB, "a", "1", "b", "2");
	}

	public void testTwoLayer() {
		checkParser(hasABCasMap, "a", "1", "b", "2", "c", Maps.makeMap("a", "3", "b", "4"));
	}

	public void testWhenKeysNotSpecified() {
		try {
			checkParser(hasABMandatory, "a", "1");
			fail();
		} catch (CannotHaveDefaultFor e) {
			assertEquals(MessageFormat.format(FastOrmMessages.cannotHaveDefaultFor, "b"), e.getMessage());
		}

	}

	public void testWithIllegalKeys() {
		try {
			checkParser(hasA, "a", "1", "b", "2");
			fail();
		} catch (CannotParseException e) {
			assertEquals(MessageFormat.format(FastOrmMessages.illegalKey, "b", Arrays.asList("a")), e.getMessage());
		}

	}

	private <K, V> void checkParser(Map<String, IStrategyNode<K, V>> idToStrategyMap, Object... attributeAndValues) {
		checkParser(new MapToXmlAttributePrinter<K, V>("root"), idToStrategyMap, attributeAndValues);
		checkParser(new MapToXmlElementPrinter<K, V>("root"), idToStrategyMap, attributeAndValues);
	}

	@SuppressWarnings("unchecked")
	private <K, V> void checkParser(IMapPrinter<K, V> printer, Map<String, IStrategyNode<K, V>> idToStrategyMap, Object... attributeAndValues) {
		// Map<String, IStrategyNode<K, V>> idToStrategy, IFunction1<Map<K, V>, V> mapToValueFn, IFunction1<String, K> transformId
		XmlToMapStrategy<K, V> strategy = new XmlToMapStrategy<K, V>(idToStrategyMap, noUnrecognisedAllowed, Functions.<Map<K, V>, V> identity(), Functions.<String, K> identity());
		Map<K, V> expected = Maps.makeMap(attributeAndValues);
		String xml = printer.print(expected);
		XmlToMapParser<K, V> parser = new XmlToMapParser<K, V>(strategy);
		Map<K, V> actual = parser.parse(xml);
		assertEquals(expected, actual);

	}

}
