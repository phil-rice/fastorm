package org.fastorm.utilities.aggregators;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.SimpleMaps;

public class SimpleMapAggregator<K, V> implements IAggregator<ISimpleMap<K, V>, ISimpleMap<K, V>> {

	private final Map<K, V> result = Collections.synchronizedMap(new HashMap<K, V>());

	public ISimpleMap<K, V> result() {
		return SimpleMaps.fromMap(result);
	}

	@Override
	public void add(ISimpleMap<K, V> t) {
		for (K key : t.keys())
			result.put(key, t.get(key));
	}

}
