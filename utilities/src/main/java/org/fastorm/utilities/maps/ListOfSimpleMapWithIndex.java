package org.fastorm.utilities.maps;

import java.util.List;

import org.fastorm.utilities.collections.Lists;

public class ListOfSimpleMapWithIndex<K, V> implements IListOfSimpleMapWithIndex<K, V> {

	private final List<ISimpleMapWithIndex<K, V>> list = Lists.newList();
	private final List<K> keys;

	public ListOfSimpleMapWithIndex(List<K> keys) {
		this.keys = keys;
	}

	public void add(ISimpleMapWithIndex<K, V> map) {
		list.add(map);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public ISimpleMapWithIndex<K, V> get(int i) {
		return list.get(i);
	}

	@Override
	public List<K> keys() {
		return keys;
	}
}
