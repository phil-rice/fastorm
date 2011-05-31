package org.fastorm.utilities.functions;

import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.SimpleMaps;

public class SimpleMapFoldFunction<K, V> implements ISymmetricFunction<ISimpleMap<K, V>> {

	@SuppressWarnings("unchecked")
	@Override
	public ISimpleMap<K, V> apply(ISimpleMap<K, V> value, ISimpleMap<K, V> initial) {
		return SimpleMaps.fromMap(SimpleMaps.<K, V> merge(value, initial));
	}
}
