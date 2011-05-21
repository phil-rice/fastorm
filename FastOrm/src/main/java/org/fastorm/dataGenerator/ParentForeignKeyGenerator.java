package org.fastorm.dataGenerator;

import java.util.List;
import java.util.Map;

import org.fastorm.defns.IEntityDefn;

public class ParentForeignKeyGenerator extends AbstractGenerator {

	public ParentForeignKeyGenerator(String childLinkColumn) {
		super(childLinkColumn);
	}

	@Override
	public void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn primary) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn parent, IEntityDefn child) {
		Map<String, Object> childItem = getLatest(data, child);
		String childIdColumn = child.getIdColumn();
		Object latestChildId = childItem.get(childIdColumn);
		add(data, parent, latestChildId);
	}
}
