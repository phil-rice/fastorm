package org.fastorm.dataSet.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.exceptions.IllegalKeyException;
import org.fastorm.mutate.IMutableItem;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.functions.IFoldFunction;

public class DrainedLine implements IMutableItem {

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
		checkSetup();
		int keyIndex = Lists.indexOf(commonData.getKeys(), key);
		return getByIndex(keyIndex);
	}

	@Override
	public Object getByIndex(int keyIndex) {
		checkSetup();
		if (keyIndex < 0)
			return null;
		else if (keyIndex < commonData.getColumnCount())
			return values[keyIndex];
		else if (values[keyIndex] == null)
			values[keyIndex] = makeDataFromChild(index, keyIndex - commonData.getColumnCount());
		return values[keyIndex];
	}

	private void checkSetup() {
		if (commonData == null)
			throw new IllegalStateException(FastOrmMessages.cannotAccessBeforeItHasBeenSetup);
	}

	private Object makeDataFromChild(int parentIndex, int childIndex) {
		IEntityDefn parentDefn = commonData.getEntityDefn();
		IEntityDefn childDefn = parentDefn.getChildren().get(childIndex);
		ISecondaryTempTableMaker maker = childDefn.getMaker();
		Object value = maker.findDataIn(commonData.getGetter(), parentDefn, parentIndex, childDefn, childIndex);
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

	@Override
	public void put(int index, Object value) {
		values[index] = value;
	}

	@Override
	public void put(String key, Object value) {
		int index = keys().indexOf(key);
		if (index == -1)
			throw new IllegalKeyException(key, keys());
		put(index, value);
	}

	@Override
	// TODO implement delete
	public void delete() {
		throw new UnsupportedOperationException();
	}
}
