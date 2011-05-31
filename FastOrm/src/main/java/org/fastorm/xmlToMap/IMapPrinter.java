package org.fastorm.xmlToMap;

import java.util.Map;

public interface IMapPrinter<K, V> {
	String print(Map<K, V> map);
}
