package org.fastorm.utilities.aggregators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.functions.IFoldFunction;
import org.fastorm.utilities.functions.IFunction1;

/** This is not a perfect implementation of a map. It will report a few things wrong if the key is present in multiple maps */
public class MapFromMaps<K, V> implements Map<K, V> {

	private final Iterable<Map<K, V>> maps;

	public MapFromMaps(Iterable<Map<K, V>> maps) {
		this.maps = maps;
	}

	public MapFromMaps(Map<K, V>... maps) {
		this.maps = Arrays.asList(maps);
	}

	@Override
	public int size() {
		int sum = 0;
		for (Map<K, V> map : maps)
			sum += map.size();
		return sum;
	}

	@Override
	public boolean isEmpty() {
		for (Map<K, V> map : maps)
			if (!map.isEmpty())
				return false;
		return true;
	}

	@Override
	public boolean containsKey(Object key) {
		for (Map<K, V> map : maps)
			if (map.containsKey(key))
				return true;
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for (Map<K, V> map : maps)
			if (map.containsValue(value))
				return true;
		return false;
	}

	@Override
	public V get(Object key) {
		for (Map<K, V> map : maps)
			if (map.containsKey(key))
				return map.get(key);
		return null;
	}

	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		Iterable<Set<K>> sets = Iterables.<Map<K, V>, Set<K>> map(maps, new IFunction1<Map<K, V>, Set<K>>() {
			@Override
			public Set<K> apply(Map<K, V> from) throws Exception {
				return from.keySet();
			}
		});
		return new SetFromSets<K>(sets);
	}

	@Override
	public Collection<V> values() {
		return Iterables.fold(new IFoldFunction<Map<K, V>, Collection<V>>() {
			@Override
			public Collection<V> apply(Map<K, V> value, Collection<V> initial) {
				initial.addAll(value.values());
				return initial;
			}
		}, maps, new ArrayList<V>());
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		Iterable<Set<Entry<K, V>>> sets = Iterables.map(maps, new IFunction1<Map<K, V>, Set<Entry<K, V>>>() {
			@Override
			public Set<Entry<K, V>> apply(Map<K, V> from) throws Exception {
				return from.entrySet();
			}
		});
		return new SetFromSets<Entry<K, V>>(sets);
	}

}
