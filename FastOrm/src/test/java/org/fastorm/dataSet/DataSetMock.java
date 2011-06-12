package org.fastorm.dataSet;

import java.util.List;

import org.fastorm.mutate.IMutableItem;
import org.fastorm.utilities.collections.ISimpleList;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.collections.SimpleLists;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;

public class DataSetMock implements IMutableDataSet {

	private final ISimpleList<ISimpleMapWithIndex<String, Object>> simpleList;
	private final List<String> keyList;

	public DataSetMock(ISimpleMapWithIndex<String, Object>... maps) {
		this.simpleList = SimpleLists.simpleList(maps);
		this.keyList = Lists.newList();
		for (int i = 0; i < simpleList.size(); i++) {
			ISimpleMap<String, Object> map = simpleList.get(i);
			Lists.addAllUnique(keyList, map.keys());
		}
	}

	@Override
	public int size() {
		return simpleList.size();
	}

	@Override
	public ISimpleMapWithIndex<String, Object> get(int i) {
		return simpleList.get(i);
	}

	@Override
	public List<String> keys() {
		return keyList;
	}

	@Override
	public IMutableItem getMutableMap(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMutableItem getMutableMapById(Object id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insert(ISimpleMap<String, Object> data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addToCollection(ISimpleMap<String, Object> primaryObject, String collectionKey, ISimpleMap<String, Object> newValues) {
		throw new UnsupportedOperationException();
	}

}
