package org.fastorm.utilities.maps;

import java.util.List;

import org.fastorm.utilities.collections.ISimpleList;

public interface IListOfSimpleMapWithIndex<K, V> extends ISimpleList<ISimpleMapWithIndex<K, V>> {

	List<K> keys();
}
