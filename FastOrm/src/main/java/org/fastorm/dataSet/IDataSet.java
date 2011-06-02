package org.fastorm.dataSet;

import org.fastorm.memory.IMemoryManager;
import org.fastorm.utilities.collections.ISimpleList;
import org.fastorm.utilities.maps.ISimpleMap;

public interface IDataSet extends ISimpleList<ISimpleMap<String, Object>> {

	IDrainedTableData getPrimaryTable();


}
