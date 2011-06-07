package org.fastorm.utilities.maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;

public class SimpleMaps {

	public static <K, V> ISimpleMap<K, V> makeMap(Object... kvs) {
		return fromMap(Maps.<K, V> makeLinkedMap(kvs));
	}

	public static <K, V> ISimpleMapWithIndex<K, V> makeMapWithIndex(Object... kvs) {
		return withIndexfromMap(Maps.<K, V> makeLinkedMap(kvs));
	}

	public static <K, V> IListOfSimpleMapWithIndex<K, V> makeList(ISimpleMapWithIndex<K, V>... maps) {
		List<K> keys = Lists.newList();
		for (ISimpleMapWithIndex<K, V> map : maps)
			Lists.addAllUnique(keys, map.keys());
		ListOfSimpleMapWithIndex<K, V> result = new ListOfSimpleMapWithIndex<K, V>(keys);
		for (ISimpleMapWithIndex<K, V> map : maps)
			result.add(map);
		return result;

	}

	public static <K, V> ISimpleMap<K, V> fromMap(final Map<K, V> map) {
		return new ISimpleMap<K, V>() {
			private final List<K> keyList = Iterables.list(map.keySet());

			@Override
			public V get(K key) {
				return map.get(key);
			}

			@Override
			public List<K> keys() {
				return keyList;
			}

		};
	}

	public static <K, V> ISimpleMapWithIndex<K, V> withIndexfromMap(final Map<K, V> map) {
		return new ISimpleMapWithIndex<K, V>() {
			private final List<K> keyList = Iterables.list(map.keySet());

			@Override
			public V get(K key) {
				return map.get(key);
			}

			@Override
			public List<K> keys() {
				return keyList;
			}

			@Override
			public V getByIndex(int keyIndex) {
				return get(keyList.get(keyIndex));
			}

		};
	}

	public static <K, V> Map<K, V> merge(Iterable<? extends ISimpleMap<K, V>> maps) {
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

	public static <K, V> void assertEquals(ISimpleMap<K, V> map, Object... expectedKeysAndValues) {
		ISimpleMap<K, V> expected = SimpleMaps.makeMap(expectedKeysAndValues);
		Assert.assertEquals(expected.keys(), map.keys());
		for (K key : expected.keys())
			Assert.assertEquals("Key: " + key, expected.get(key), map.get(key));

	}
}
