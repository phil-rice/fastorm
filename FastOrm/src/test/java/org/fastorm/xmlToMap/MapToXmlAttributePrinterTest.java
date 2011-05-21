package org.fastorm.xmlToMap;

import java.util.Map;

import junit.framework.TestCase;

import org.fastorm.utilities.Maps;

public class MapToXmlAttributePrinterTest extends TestCase {
	public void testSimpleMapToXml() {
		check("<Root></Root>");
		check("<Root a='1' b='2' ></Root>", "a", 1, "b", 2);
		check("<Root a='1' b='2' ><c d='4' ></c></Root>", "a", 1, "b", 2, "c", Maps.makeLinkedMap("d", 4));
	}

	private void check(String expected, Object... keysAndValues) {
		Map<Object, Object> map = Maps.makeLinkedMap(keysAndValues);
		String actual = new MapToXmlAttributePrinter<Object, Object>("Root").print(map);
		assertEquals(expected, actual);
	}
}
