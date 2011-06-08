package org.fastorm.memory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fastorm.api.IJobDetails;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.dataSet.impl.DrainedLine;
import org.fastorm.dataSet.impl.DrainedLineCommonData;
import org.fastorm.dataSet.impl.DrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.pooling.api.IPool;
import org.fastorm.pooling.api.PoolOptions;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.maps.Maps;

public class MemoryManager implements IMemoryManager {

	private final Map<IEntityDefn, IPool<DrainedLine>> lineMap = Maps.newMap();
	private final Map<IEntityDefn, IPool<DrainedTableData>> tableMap = Maps.newMap();
	private final List<IPool<?>> allPools = Lists.newList();
	private final IJobDetails jobDetails;
	private final PoolOptions poolOptions;

	public MemoryManager() {
		this.jobDetails = null;
		this.poolOptions = new PoolOptions();
	}

	public MemoryManager(IJobDetails jobDetails, PoolOptions poolOptions) {
		this.jobDetails = jobDetails;
		this.poolOptions = poolOptions;
	}

	public IMemoryManager withJobDetails(IJobDetails jobDetails) {
		return new MemoryManager(jobDetails, poolOptions);
	}

	@Override
	public void dispose() {
		for (int i = 0; i < allPools.size(); i++)
			allPools.get(i).dispose();
	}

	@Override
	public DrainedLine makeDrainedLine(DrainedLineCommonData commonData, ResultSet rs, int index) throws SQLException {
		DrainedLine line = linePool(commonData.getEntityDefn()).newObject();
		line.setValuesFrom(commonData, rs, index);
		return line;
	}

	@Override
	public DrainedTableData makeDrainedTableData(IMemoryManager memoryManager, IEntityDefn entityDefn, IGetDrainedTableForEntityDefn getter, ResultSet rs) {
		DrainedTableData data = tablePool(entityDefn).newObject();
		data.setData(memoryManager, entityDefn, getter, rs);
		return data;
	}

	private IPool<DrainedLine> linePool(final IEntityDefn defn) {
		return findOrCreatePool(defn, lineMap, new Callable<IPool<DrainedLine>>() {
			@Override
			public IPool<DrainedLine> call() throws Exception {
				int linesSize = getLinesSize(defn);
				return IMemoryManager.Utils.makeDrainedLinePool(poolOptions.withMaxObjects(linesSize));
			}
		});
	}

	private IPool<DrainedTableData> tablePool(final IEntityDefn defn) {
		return findOrCreatePool(defn, tableMap, new Callable<IPool<DrainedTableData>>() {
			@Override
			public IPool<DrainedTableData> call() throws Exception {
				int poolSize = IEntityDefn.Utils.countOfSelfAndDescendents(defn);
				int linesSize = getLinesSize(defn);
				return IMemoryManager.Utils.makeDrainedTableDataPool(poolOptions.withMaxObjects(poolSize), linesSize);
			}

		});
	}

	private int getLinesSize(final IEntityDefn defn) {
		Map<String, String> parameters = defn.parameters();
		String string = parameters.get(FastOrmKeys.maxLinesPerBatch);
		Integer maxLinesPerBatch = Integer.parseInt(string);
		int linesSize = jobDetails.getBatchSize() * maxLinesPerBatch;
		return linesSize;
	}

	private <T> IPool<T> findOrCreatePool(IEntityDefn defn, Map<IEntityDefn, IPool<T>> map, final Callable<IPool<T>> createPool) {
		return Maps.findOrCreate(map, defn, new Callable<IPool<T>>() {
			@Override
			public IPool<T> call() throws Exception {
				IPool<T> result = createPool.call();
				allPools.add(result);
				return result;
			}
		});
	}

	@Override
	public IMemoryManager withPoolOptions(PoolOptions poolOptions) {
		return new MemoryManager(jobDetails, poolOptions);
	}

}
