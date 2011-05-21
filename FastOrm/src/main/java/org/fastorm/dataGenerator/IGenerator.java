package org.fastorm.dataGenerator;

import java.util.List;
import java.util.Map;

import org.fastorm.defns.IEntityDefn;

public interface IGenerator {

	void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn primary);

	void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn parent, IEntityDefn child);

}
