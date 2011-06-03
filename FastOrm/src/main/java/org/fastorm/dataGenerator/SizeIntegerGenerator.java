package org.fastorm.dataGenerator;

import java.util.List;
import java.util.Map;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.maps.Maps;

public class SizeIntegerGenerator extends AbstractGenerator {

	public SizeIntegerGenerator(String columnName) {
		super(columnName);
	}

	@Override
	public void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn primary) {
		List<Map<String, Object>> existing = data.get(primary);
		int size = existing.size() + Maps.intFor(sizeMap, primary);
		add(data, primary, size);
	}

}
