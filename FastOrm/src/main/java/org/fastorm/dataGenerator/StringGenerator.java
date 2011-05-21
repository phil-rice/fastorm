package org.fastorm.dataGenerator;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.Maps;

public class StringGenerator extends AbstractGenerator {

	public StringGenerator(String columnName) {
		super(columnName);
	}

	private String pattern;
	private Random random = new Random();

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn entityDefn) {
		List<Map<String, Object>> existing = data.get(entityDefn);
		int size = existing.size() + Maps.intFor(sizeMap, entityDefn);
		add(data, entityDefn, MessageFormat.format(pattern, size, random.nextInt(1000)));
	}

}
