package org.fastorm.dataGenerator;

import java.util.List;
import java.util.Map;

import org.fastorm.defns.IEntityDefn;

public interface IDataGeneratorVisitor {
	void acceptPrimary(Map<IEntityDefn, List<Map<String, Object>>> data) throws Exception;
	

}
