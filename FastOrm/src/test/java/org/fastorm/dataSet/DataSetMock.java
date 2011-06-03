package org.fastorm.dataSet;

import java.util.List;

import org.fastorm.utilities.collections.ISimpleList;
import org.fastorm.utilities.collections.SimpleLists;
import org.fastorm.utilities.maps.ISimpleMap;

public class DataSetMock implements IDataSet {

	private final ISimpleList<ISimpleMap<String, Object>> simpleList;

	public DataSetMock(ISimpleMap<String, Object>... maps) {
		this.simpleList = SimpleLists.simpleList(maps);
	}

	@Override
	public int size() {
		return simpleList.size();
	}

	@Override
	public ISimpleMap<String, Object> get(int index) {
		return simpleList.get(index);
	}

	@Override
	public List<ISimpleMap<String, Object>> slowList() {
		return simpleList.slowList();
	}

	@Override
	public IDrainedTableData getPrimaryTable() {
		throw new UnsupportedOperationException();
	}

}
