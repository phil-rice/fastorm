package org.fastorm.dataSet;

import org.fastorm.memory.IMemoryManager;
import org.fastorm.utilities.ISimpleList;
import org.fastorm.utilities.ISimpleMap;

public interface IDataSet extends ISimpleList<ISimpleMap<String, Object>> {

	IDrainedTableData getPrimaryTable();

	void dispose(IMemoryManager memoryManager);

}
