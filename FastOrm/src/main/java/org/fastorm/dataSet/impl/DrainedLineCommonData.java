package org.fastorm.dataSet.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.maps.ISimpleMap;

public class DrainedLineCommonData {
	private IMemoryManager memoryManager;
	private IGetDrainedTableForEntityDefn getter;
	private IEntityDefn entityDefn;
	private int childCount;
	private List<String> keys;
	private int columnCount;

	public DrainedLineCommonData(IMemoryManager memoryManager, IEntityDefn entityDefn) {
		this.memoryManager = memoryManager;
		this.entityDefn = entityDefn;
		List<IEntityDefn> children = entityDefn.getChildren();
		this.childCount = children.size();
	}

	public void setData(IGetDrainedTableForEntityDefn getter, ResultSet resultSet) throws SQLException {
		this.getter = getter;
		if (keys == null) {
			ResultSetMetaData metaData = resultSet.getMetaData();
			columnCount = metaData.getColumnCount();
			keys = Lists.newList(columnCount + childCount);
			for (int i = 1; i <= columnCount; i++)
				keys.add(metaData.getColumnLabel(i));
			for (int i = 1; i <= childCount; i++) {
				List<IEntityDefn> children = entityDefn.getChildren();
				keys.add(children.get(i).getEntityName());
			}
		}
		assert sizesMatch(resultSet);
	}

	private boolean sizesMatch(ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		return columnCount == metaData.getColumnCount();
	}

	public IGetDrainedTableForEntityDefn getGetter() {
		return getter;
	}

	public IEntityDefn getEntityDefn() {
		return entityDefn;
	}

	public IMemoryManager getMemoryManager() {
		return memoryManager;
	}

	public int getChildCount() {
		return childCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public List<String> getKeys() {
		return keys;
	}
}
