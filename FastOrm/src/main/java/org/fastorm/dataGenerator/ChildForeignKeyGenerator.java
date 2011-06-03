package org.fastorm.dataGenerator;

import java.util.List;
import java.util.Map;

import org.fastorm.defns.IEntityDefn;

public class ChildForeignKeyGenerator extends AbstractGenerator {

	public ChildForeignKeyGenerator(String columnName) {
		super(columnName);
	}

	@Override
	public void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn primary) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void contribute(Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn parent, IEntityDefn child) {
		Object parentIdColumn = parent.getIdColumn();
		Object latestParentId = getLatest(data, parent).get(parentIdColumn);
		add(data, child, latestParentId);

	}

}
