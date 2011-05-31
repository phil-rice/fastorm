package org.fastorm.dataGenerator;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.constants.UtilityMessages;

public abstract class AbstractGenerator implements IGenerator {
	protected String columnName;

	public AbstractGenerator(String columnName) {
		this.columnName = columnName;
	}

	public void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn parent, IEntityDefn child) {
		contribute(data, sizeMap, child);
	}

	protected void add(Map<IEntityDefn, List<Map<String, Object>>> data, IEntityDefn entityDefn, Object value) {
		Map<String, Object> map = getLatest(data, entityDefn);
		if (map.containsKey(columnName))
			throw new IllegalStateException(MessageFormat.format(UtilityMessages.duplicateKey, columnName, map.get(columnName), value));
		map.put(columnName, value);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[columnName=" + columnName + "]";
	}

	protected Map<String, Object> getLatest(Map<IEntityDefn, List<Map<String, Object>>> data, IEntityDefn entityDefn) {
		List<Map<String, Object>> parentList = data.get(entityDefn);
		Map<String, Object> parentItem = parentList.get(parentList.size() - 1);
		return parentItem;
	}
}
