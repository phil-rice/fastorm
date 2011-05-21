package org.fastorm.xmlToMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.fastorm.utilities.Maps;

public class MapToXmlAttributePrinter<K, V> implements IMapPrinter<K, V> {

	private final String rootElement;

	public MapToXmlAttributePrinter(String rootElement) {
		this.rootElement = rootElement;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String print(Map<K, V> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("<");
		builder.append(rootElement);
		Map<Class, Map<K, V>> split = Maps.partitionByValueClass(map, LinkedHashMap.class, Map.class, Object.class);
		Map<K, V> simpleAttributes = split.get(Object.class);
		if (simpleAttributes != null) {
			builder.append(" ");
			for (Entry<K, V> entry : simpleAttributes.entrySet()) {
				builder.append(entry.getKey());
				builder.append("='");
				builder.append(entry.getValue());
				builder.append("' ");
			}
		}
		builder.append(">");
		Map<K, V> nestedAttributes = split.get(Map.class);
		if (nestedAttributes != null)
			for (Entry<K, V> entry : nestedAttributes.entrySet())
				builder.append(new MapToXmlAttributePrinter(entry.getKey().toString()).print((Map) entry.getValue()));
		builder.append("</");
		builder.append(rootElement);
		builder.append(">");
		return builder.toString();
	}
}
