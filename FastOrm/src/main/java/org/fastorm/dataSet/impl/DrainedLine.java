package org.fastorm.dataSet.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.functions.IFoldFunction;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;

public class DrainedLine implements ISimpleMapWithIndex<String, Object> {

	private DrainedLineCommonData commonData;
	private int index;
	private Object[] values;

	public void setValuesFrom(DrainedLineCommonData commonData, ResultSet rs, int index) throws SQLException {
		this.commonData = commonData;
		this.index = index;
		int columnCount = commonData.getColumnCount();
		int childCount = commonData.getChildCount();
		if (values == null)
			values = new Object[columnCount + childCount];
		assert values.length == columnCount + childCount;
		for (int i = 0; i < columnCount; i++)
			this.values[i] = rs.getObject(i + 1);
		for (int i = 0; i < childCount; i++)
			this.values[i + columnCount] = null;
	}

	@Override
	public Object get(String key) {
		int keyIndex = Lists.indexOf(commonData.getKeys(), key);
		return getByIndex(keyIndex);
	}

	@Override
	public Object getByIndex(int keyIndex) {
		if (keyIndex < 0)
			return null;
		else if (keyIndex < commonData.getColumnCount())
			return values[keyIndex];
		else if (values[keyIndex] == null)
			values[keyIndex] = makeDataFromChild(index, keyIndex - commonData.getColumnCount());
		return values[keyIndex];
	}

	private Object makeDataFromChild(int parentIndex, int childIndex) {
		IEntityDefn parentDefn = commonData.getEntityDefn();
		IEntityDefn childDefn = parentDefn.getChildren().get(childIndex);
		Object value = childDefn.getMaker().findDataIn(commonData.getGetter(), parentDefn, parentIndex, childDefn, childIndex);
		return value;
	}

	@Override
	public List<String> keys() {
		assert commonData.getKeys() != null;
		return commonData.getKeys();
	}

	public void clean() {
		commonData = null;
		for (int i = 0; i < values.length; i++)
			values[i] = null;
	}

	@Override
	public String toString() {
		String fold = Iterables.fold(new IFoldFunction<String, String>() {
			@Override
			public String apply(String value, String initial) {
				String comma = initial.length() == 0 ? "" : ",";
				return initial + comma + value + "=" + get(value);
			}
		}, commonData.getKeys(), "");
		return "DrainedLine [index=" + index + ", values=" + fold + "]";
	}
}
