package org.fastorm.utilities.maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;

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

	public static <K, V, P> Map<P, Map<K, V>> partitionByKey(ISimpleMap<K, V> input, IFunction1<K, P> partitionFunction) {
		try {
			Map<P, Map<K, V>> result = Maps.newMap();
			for (K key : input.keys()) {
				V value = input.get(key);
				P partition = partitionFunction.apply(key);
				Maps.addToMapOfLinkedMaps(result, partition, key, value);
			}
			return result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public static <K, V, P> Map<P, Map<K, V>> partitionByValue(ISimpleMap<K, V> input, IFunction1<V, P> partitionFunction) {
		try {
			Map<P, Map<K, V>> result = Maps.newMap();
			for (K key : input.keys()) {
				V value = input.get(key);
				P partition = partitionFunction.apply(value);
				Maps.addToMapOfLinkedMaps(result, partition, key, value);
			}
			return result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

}
