package org.fastorm.sqlDialects;

import java.util.Map;
import java.util.Set;

import org.fastorm.api.FastOrmOptions;

public interface ISqlStrings {
	Set<String> getTemplateNames();

	String getFromTemplate(FastOrmOptions options, String templateName, Map<String, ? extends Object> parameters, Object... other);
}
