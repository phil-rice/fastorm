package org.fastorm.xmlToMap;

import java.util.Map;

import junit.framework.TestCase;

import org.fastorm.utilities.maps.Maps;

public class MapToXmlElementPrinterTest extends TestCase {

	public void testSimpleMapToXml() {
		check("<Root></Root>");
		check("<Root><a>1</a><b>2</b></Root>", "a", 1, "b", 2);
		check("<Root><a>1</a><b>2</b><c><d>4</d></c></Root>", "a", 1, "b", 2, "c", Maps.makeLinkedMap("d", 4));
	}

	private void check(String expected, Object... keysAndValues) {
		Map<Object, Object> map = Maps.makeLinkedMap(keysAndValues);
		String actual = new MapToXmlElementPrinter<Object, Object>("Root").print(map);
		assertEquals(expected, actual);
	}

}
