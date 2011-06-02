package org.fastorm.memory;

import java.util.List;

import org.fastorm.dataSet.impl.DrainedLine;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.pooling.api.IPool;

public interface IMemoryManager {
	void dispose();

	IPool<DrainedLine> mapPool(IEntityDefn defn, List<String> keys);

}
