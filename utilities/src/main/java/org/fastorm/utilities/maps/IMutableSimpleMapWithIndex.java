package org.fastorm.utilities.maps;

public interface IMutableSimpleMapWithIndex<K, V> extends ISimpleMapWithIndex<K, V> {

	void put(int index, V value);

	void put(K key, V value);

	void delete();
}
