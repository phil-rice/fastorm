package org.fastorm.dataSet;

import java.util.List;

import org.fastorm.memory.IMemoryManager;
import org.fastorm.utilities.collections.ISimpleList;
import org.fastorm.utilities.collections.SimpleLists;
import org.fastorm.utilities.maps.ISimpleMap;

public class DataSetMock implements IDataSet {

	private final ISimpleList<ISimpleMap<String, Object>> simpleList;

	public DataSetMock(ISimpleMap<String, Object>... maps) {
		this.simpleList = SimpleLists.simpleList(maps);
	}

	public int size() {
		return simpleList.size();
	}

	public ISimpleMap<String, Object> get(int index) {
		return simpleList.get(index);
	}

	public List<ISimpleMap<String, Object>> slowList() {
		return simpleList.slowList();
	}

	public IDrainedTableData getPrimaryTable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void dispose(IMemoryManager memoryManager) {
	}
}
