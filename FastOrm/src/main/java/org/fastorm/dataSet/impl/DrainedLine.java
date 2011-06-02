package org.fastorm.dataSet.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.maps.ISimpleMap;

public class DrainedLine implements ISimpleMap<String, Object> {

	private DrainedLineCommonData commonData;
	private int index;
	private Object[] values;

	public DrainedLine(int width) {
		this.values = new Object[width];
	}

	public void setValuesFrom(DrainedLineCommonData commonData, ResultSet rs, int index) throws SQLException {
		this.commonData = commonData;
		this.index = index;
		int columnCount = commonData.getColumnCount();
		for (int i = 0; i < columnCount; i++)
			this.values[i] = rs.getObject(i + 1);
		int childCount = commonData.getChildCount();
		for (int i = 0; i < childCount; i++)
			this.values[i + columnCount] = null;
	}

	@Override
	public Object get(String key) {
		int keyIndex = Lists.indexOf(commonData.getKeys(), key);
		if (keyIndex == -1)
			return null;
		else if (keyIndex < commonData.getColumnCount())
			return values[keyIndex];
		else if (values[keyIndex] == null)
			values[keyIndex] = makeDataFromChild(keyIndex - commonData.getColumnCount());
		return values[keyIndex];
	}

	private Object makeDataFromChild(int childIndex) {
		IEntityDefn parentDefn = commonData.getEntityDefn();
		IEntityDefn childDefn = parentDefn.getChildren().get(childIndex);
		Object value = childDefn.getMaker().findDataIn(commonData.getGetter(), parentDefn, childDefn, index);
		return value;
	}

	@Override
	public List<String> keys() {
		assert commonData.getKeys() != null;
		return commonData.getKeys();
	}

}
