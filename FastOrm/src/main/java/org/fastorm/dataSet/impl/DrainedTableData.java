package org.fastorm.dataSet.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.pooling.api.IPool;
import org.fastorm.utilities.annotations.TightLoop;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.maps.ArraySimpleMap;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.Maps;

public class DrainedTableData implements IDrainedTableData {

	private  IEntityDefn entityDefn;
	private List<String> columnNames;
	private int idColumnIndex = -1;
	private List<ISimpleMap<String, Object>> data;
	private int columnCount;
	private Map<Integer, Map<Object, List<ISimpleMap<String, Object>>>> indices;

	@Override
	public String toString() {
		return "DrainedTableData [entityDefn=" + entityDefn.getEntityName() + ", data=" + data.size() + "]";
	}

	public DrainedTableData(){
	}
	public void setData( IMemoryManager memoryManager,IEntityDefn entityDefn, IGetDrainedTableForEntityDefn getter, ResultSet rs ) {
		this.entityDefn = entityDefn;
		DrainedLineCommonData commonData = new DrainedLineCommonData();commonData.setData(memoryManager, getter, entityDefn);
		data = Lists.newList();
		try {
			String idColumn = entityDefn.getIdColumn();
			assert idColumn != null;
			ResultSetMetaData metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			columnNames = new ArrayList<String>(columnCount);
			for (int i = 0; i < columnCount; i++) {
				String columnName = metaData.getColumnName(i + 1);
				columnNames.add(columnName);
				if (columnName.equals(idColumn))
					idColumnIndex = i;
			}
			for (IEntityDefn child : entityDefn.getChildren())
				columnNames.add(child.getEntityName());
			IPool<DrainedLine> pool = memoryManager.mapPool(entityDefn, columnNames);
			int index = 0;
			while (rs.next()) {
				DrainedLine line = pool.newObject();
				line.setValuesFrom(commonData, rs, index);
				data.add(line);
			}
			return;
		} catch (SQLException e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public IEntityDefn getEntityDefn() {
		return entityDefn;
	}

	@Override
	public int getIdColumnIndex() {
		return idColumnIndex;
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public List<String> getColumnNames() {
		return columnNames;
	}

	@Override
	@TightLoop
	public List<ISimpleMap<String, Object>> findWith(IGetDrainedTableForEntityDefn getter, int columnIndex, Object value) {
		if (indices == null)
			indices = Maps.newMap();
		Map<Object, List<ISimpleMap<String, Object>>> map = indices.get(columnIndex);
		if (map == null) {
			map = new HashMap<Object, List<ISimpleMap<String, Object>>>();
			indices.put(columnIndex, map);
			for (int i = 0; i < data.size(); i++) {
				Object[] item = data.get(i);
				Maps.addToList(map, item[columnIndex], getMap(getter, i));
			}
		}
		List<ISimpleMap<String, Object>> result = map.get(value);
		return result == null ? Collections.<ISimpleMap<String, Object>> emptyList() : result;
	}

}
