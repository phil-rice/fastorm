package org.fastorm.memory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fastorm.dataSet.impl.DrainedLine;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.pooling.api.IPool;
import org.fastorm.pooling.api.PoolOptions;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.maps.ArraySimpleMap;
import org.fastorm.utilities.maps.Maps;

public class MemoryManager implements IMemoryManager {

	public final static int maxMapSize = 1000000;
	private Map<IEntityDefn, IPool<DrainedLine>> map = Maps.newMap();
	private List<IPool<DrainedLine>> pools = Lists.newList();

	@Override
	public void dispose() {
		for (int i = 0; i < pools.size(); i++)
			pools.get(i).dispose();
	}

	@Override
	public IPool<DrainedLine> mapPool(IEntityDefn defn, final List<String> keys) {
		return Maps.findOrCreate(map, defn, new Callable<IPool<DrainedLine>>() {
			@Override
			public IPool<DrainedLine> call() throws Exception {
				PoolOptions poolOptions = new PoolOptions(false, maxMapSize, false);
				IPool<DrainedLine> result = IPool.Utils.makeArraySimpleMapPool(poolOptions, DrainedLine.class, keys, Object.class);
				pools.add(result);
				return result;
			}
		});
	}
}
