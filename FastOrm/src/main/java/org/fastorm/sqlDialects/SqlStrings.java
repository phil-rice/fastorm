package org.fastorm.sqlDialects;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmOptions;
import org.fastorm.utilities.Maps;
import org.fastorm.utilities.WrappedException;
import org.springframework.core.io.Resource;

public class SqlStrings implements ISqlStrings {

	@Override
	public String toString() {
		return "SqlStrings [templateGroup=" + templateGroup.getName() + "]";
	}

	private final StringTemplateGroup templateGroup;

	public SqlStrings(Resource resource) {
		try {
			templateGroup = new StringTemplateGroup(new InputStreamReader(resource.getInputStream()));
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<String> getTemplateNames() {
		return templateGroup.getTemplateNames();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public String getFromTemplate(FastOrmOptions options, String templateName, Map<String, ? extends Object> map, Object... other) {
		StringTemplate template = templateGroup.getInstanceOf(templateName);
		Map formalArguments = template.getFormalArguments();
		template.setAttributes(Maps.newMap());
		for (Entry<String, ? extends Object> entry : map.entrySet())
			if (formalArguments.containsKey(entry.getKey()))
				template.setAttribute(entry.getKey(), entry.getValue());
		for (int i = 0; i < other.length; i += 2)
			template.setAttribute((String) other[i + 0], other[i + 1]);
		if (!options.useTemporaryTables)
			template.removeAttribute(FastOrmKeys.useTemporaryTable);
		assert assertInAllParametersSet(template);
		return template.toString();
	}

	@SuppressWarnings("rawtypes")
	private boolean assertInAllParametersSet(StringTemplate template) {
		Map formalArguments = template.getFormalArguments();
		Map attributes = template.getAttributes();
		List<Object> missing = new ArrayList<Object>();
		for (Object key : formalArguments.keySet())
			if (!attributes.containsKey(key))
				missing.add(key);
		boolean result = missing.size() == 0;
		assert result : missing;
		return result;
	}

}
