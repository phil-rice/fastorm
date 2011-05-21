package org.fastorm.utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleMaps {

	public static <K, V> ISimpleMap<K, V> makeMap(Object... kvs) {
		return fromMap(Maps.<K, V> makeMap(kvs));
	}

	public static <K, V> ISimpleMap<K, V> fromMap(final Map<K, V> map) {
		return new ISimpleMap<K, V>() {
			@Override
			public V get(K key) {
				return map.get(key);
			}

			@Override
			public List<K> keys() {
				return Iterables.list(map.keySet());
			}
		};
	}

	public static <K, V> Map<K, V> merge(Iterable<ISimpleMap<K, V>> maps) {
		Map<K, V> result = new HashMap<K, V>();
		for (ISimpleMap<K, V> map : maps)
			for (K key : map.keys())
				result.put(key, map.get(key));
		return result;
	}

	public static <K, V> Map<K, V> merge(ISimpleMap<K, V>... maps) {
		Map<K, V> result = new HashMap<K, V>();
		for (ISimpleMap<K, V> map : maps)
			for (K key : map.keys())
				result.put(key, map.get(key));
		return result;
	}

	public static <K, V> ISimpleMap<K, V> empty() {
		return new ISimpleMap<K, V>() {
			@Override
			public V get(K key) {
				return null;
			}

			@Override
			public List<K> keys() {
				return Collections.emptyList();
			}
		};
	}

}
