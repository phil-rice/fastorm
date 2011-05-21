package org.fastorm.xmlToMap;

import java.util.Map;
import java.util.Map.Entry;

public class MapToXmlElementPrinter<K, V> implements IMapPrinter<K, V> {

	private final String rootElement;

	public MapToXmlElementPrinter(String rootElement) {
		this.rootElement = rootElement;

	}

	@SuppressWarnings({ "unchecked" })
	public String print(Map<K, V> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("<");
		builder.append(rootElement);
		builder.append(">");
		for (Entry<K, V> entry : map.entrySet()) {
			K key = entry.getKey();
			V value = entry.getValue();
			if (value instanceof Map) {
				builder.append(new MapToXmlElementPrinter<K, V>(key.toString()).print((Map<K, V>) value));
			} else {
				builder.append("<");
				builder.append(key);
				builder.append(">");
				builder.append(value);
				builder.append("</");
				builder.append(key);
				builder.append(">");
			}
		}
		builder.append("</");
		builder.append(rootElement);
		builder.append(">");
		return builder.toString();
	}

}
