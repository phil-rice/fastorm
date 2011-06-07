package org.fastorm.sqlDialects;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.utilities.maps.IListOfSimpleMapWithIndex;

public interface ISqlStrings {
	Set<String> getTemplateNames();

	String getFromTemplate(FastOrmOptions options, String templateName, Map<String, ? extends Object> parameters, Object... other);

	String buildInsert(String tableName, IListOfSimpleMapWithIndex<String, Object> data);

	String buildInsert(String tableName, IListOfSimpleMapWithIndex<String, Object> data, List<Integer> changedColumns);

}
