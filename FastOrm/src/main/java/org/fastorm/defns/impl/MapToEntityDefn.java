package org.fastorm.defns.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.temp.ITempTableMakerFactory;
import org.fastorm.utilities.maps.Maps;

public class MapToEntityDefn {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IEntityDefn create(ITempTableMakerFactory factory, Map<String, String> parentParameters, Map<String, Object> from) {
		Map<Class, Map<String, Object>> partitionedMap = Maps.partitionByValueClass(from, LinkedHashMap.class, Map.class, Object.class);
		List<IEntityDefn> childList = new ArrayList<IEntityDefn>();
		Map<String, String> parameters = (Map) partitionedMap.get(Object.class);
		Map<String, Object> maps = partitionedMap.get(Map.class);
		if (maps != null)
			for (Entry<String, Object> entry : maps.entrySet()) {
				Map<String, Object> value = (Map<String, Object>) entry.getValue();
				childList.add(create(factory, parameters, value));
			}
		ISecondaryTempTableMaker maker = parentParameters == null ? null : factory.findReaderMakerFor(parentParameters, parameters);
		return new EntityDefn(maker, Collections.unmodifiableMap(parameters), Collections.unmodifiableList(childList));
	}
}
