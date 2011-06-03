package org.fastorm.utilities.maps;

public interface ISimpleMapWithIndex<K, V> extends ISimpleMap<K, V> {

	V getByIndex(int keyIndex);
}
