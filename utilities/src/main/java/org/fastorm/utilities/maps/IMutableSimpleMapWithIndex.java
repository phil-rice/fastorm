package org.fastorm.utilities.maps;

public interface IMutableSimpleMapWithIndex<K, V> extends ISimpleMapWithIndex<K, V> {

	void put(K key, V value);

	void delete();
}
