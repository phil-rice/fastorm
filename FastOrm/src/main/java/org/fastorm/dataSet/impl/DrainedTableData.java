package org.fastorm.dataSet.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fastorm.api.IJobDetails;
import org.fastorm.constants.FastOrmMessages;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.mutate.IMutableItem;
import org.fastorm.utilities.annotations.TightLoop;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;
import org.fastorm.utilities.maps.Maps;

public class DrainedTableData implements IDrainedTableData {

	private DrainedLineCommonData commonData;
	private final DrainedLine[] data;
	private Map<Integer, Map<Object, List<ISimpleMap<String, Object>>>> indices;
	private int index;

	public DrainedTableData(int maxSize) {
		this.data = new DrainedLine[maxSize];
	}

	@Override
	public String toString() {
		String entityName = commonData == null ? null : getEntityDefn().getEntityName();
		return "DrainedTableData [entityDefn=" + entityName + ", data=" + size() + "]";
	}

	public void setData(IMemoryManager memoryManager, IJobDetails jobDetails, IEntityDefn entityDefn, IGetDrainedTableForEntityDefn getter, ResultSet rs) {
		try {
			commonData = new DrainedLineCommonData(memoryManager, jobDetails, entityDefn);
			commonData.setData(getter, rs.getMetaData());
			index = 0;
			while (rs.next()) {
				data[index] = memoryManager.makeDrainedLine(commonData, rs, index);
				index++;
			}
			return;
		} catch (SQLException e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public IEntityDefn getEntityDefn() {
		checkSetup();
		return commonData.getEntityDefn();
	}

	@Override
	public int getIdColumnIndex() {
		checkSetup();
		return commonData.getIdColumnIndex();
	}

	@Override
	public int size() {
		return index;
	}

	@Override
	@TightLoop
	// TODO Need to convert this to be pooled
	public List<ISimpleMap<String, Object>> findWith(int columnIndex, Object value) {
		if (indices == null)
			indices = Maps.newMap();
		Map<Object, List<ISimpleMap<String, Object>>> map = indices.get(columnIndex);
		if (map == null) {
			map = new HashMap<Object, List<ISimpleMap<String, Object>>>();
			indices.put(columnIndex, map);
			for (int i = 0; i < index; i++) {
				DrainedLine item = data[i];
				Object key = item.getByIndex(columnIndex);
				ISimpleMapWithIndex<String, Object> itemValue = get(i);
				Maps.addToList(map, key, itemValue);
			}
		}
		List<ISimpleMap<String, Object>> result = map.get(value);
		return result == null ? Collections.<ISimpleMap<String, Object>> emptyList() : result;
	}

	public void clean() {
		if (indices != null)
			indices.clear();
		index = 0;
	}

	@Override
	public int indexOf(String key) {
		checkSetup();
		return commonData.getKeys().indexOf(key);
	}

	@Override
	public List<String> keys() {
		checkSetup();
		return commonData.getKeys();
	}

	private void checkSetup() {
		if (commonData == null)
			throw new IllegalStateException(FastOrmMessages.cannotAccessBeforeItHasBeenSetup);
	}

	@Override
	public IMutableItem get(int index) {
		return data[index];
	}

}
