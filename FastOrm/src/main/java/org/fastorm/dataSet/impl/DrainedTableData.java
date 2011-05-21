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
import org.fastorm.utilities.ISimpleMap;
import org.fastorm.utilities.Maps;
import org.fastorm.utilities.Optimised;
import org.fastorm.utilities.WrappedException;

public class DrainedTableData implements IDrainedTableData {

	private final IEntityDefn entityDefn;
	private List<String> columnNames;
	private int idColumnIndex = -1;
	private ArrayList<Object[]> data;
	private int columnCount;
	private Map<Integer, Map<Object, List<ISimpleMap<String, Object>>>> indices;

	public DrainedTableData(IMemoryManager memoryManager, IEntityDefn entityDefn, ResultSet rs) {
		data = new ArrayList<Object[]>();
		try {
			this.entityDefn = entityDefn;
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
			while (rs.next()) {
				Object[] line = memoryManager.array(columnCount);
				for (int i = 0; i < columnCount; i++)
					line[i] = rs.getObject(i + 1);
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
	public Object[] getLine(int i) {
		return data.get(i);
	}

	@Override
	public List<String> getColumnNames() {
		return columnNames;
	}

	@Override
	@Optimised
	public ISimpleMap<String, Object> getMap(final IGetDrainedTableForEntityDefn getter, final int index) {
		return new ISimpleMap<String, Object>() {
			@Override
			public Object get(String key) {
				@Optimised
				int indexOf = columnNames.indexOf(key);
				if (indexOf == -1)
					throw new IllegalArgumentException(MessageFormat.format(FastOrmMessages.noValueForKey, key, this));
				if (indexOf >= columnCount) {
					IEntityDefn child = findChildWithName(key);
					IDrainedTableData childTableData = getter.get(child);
					List<ISimpleMap<String, Object>> childData = child.getMaker().findDataIn(getter, DrainedTableData.this, childTableData, child, index);
					return childData;
				}
				return getLine(index)[indexOf];
			}

			private IEntityDefn findChildWithName(String childName) {
				int childCount = entityDefn.getChildren().size();
				for (int i = 0; i < childCount; i++) {
					IEntityDefn child = entityDefn.getChildren().get(i);
					if (childName.equals(child.getEntityName())) {
						return child;
					}
				}
				throw new IllegalStateException(MessageFormat.format(FastOrmMessages.cannotFindChildTableWithName, entityDefn.getEntityName(), childName, entityDefn));
			}

			@Override
			public List<String> keys() {
				return columnNames;
			}

			@Override
			public String toString() {
				return Maps.fromSimpleMap(this).toString();
			}
		};
	}

	@Override
	@Optimised
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

	@Override
	public void dispose(IMemoryManager memoryManager) {
		ArrayList<Object[]> oldData = data;
		data = null;
		for (int i = 0; i < oldData.size(); i++)
			memoryManager.finishedWith(oldData.get(i));
	}
}
