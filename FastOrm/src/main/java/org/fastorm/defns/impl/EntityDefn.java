package org.fastorm.defns.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.fastorm.constants.FastOrmKeys;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.ISecondaryTempTableMaker;

public class EntityDefn implements IEntityDefn {

	@Override
	public String toString() {
		return "EntityDefn [" + parameters.get(FastOrmKeys.tableName) + "]";
	}

	private final Map<String, String> parameters;
	private final List<IEntityDefn> children;
	private final ISecondaryTempTableMaker maker;

	public EntityDefn(ISecondaryTempTableMaker maker, Map<String, String> parameters, List<IEntityDefn> children) {
		this.maker = maker;
		this.parameters = parameters;
		this.children = Collections.unmodifiableList(new ArrayList<IEntityDefn>(children));
	}

	@SuppressWarnings("unchecked")
	public EntityDefn() {
		this.parameters = Collections.EMPTY_MAP;
		this.children = Collections.EMPTY_LIST;
		this.maker = null;
	}

	@Override
	public List<IEntityDefn> getChildren() {
		return children;
	}

	@Override
	public Map<String, String> parameters() {
		return parameters;
	}

	@Override
	public String getEntityName() {
		return parameters.get(FastOrmKeys.entityName);
	}

	@Override
	public String getTableName() {
		return parameters.get(FastOrmKeys.tableName);
	}

	@Override
	public String getTempTableName() {
		return parameters.get(FastOrmKeys.tempTableName);
	}

	@Override
	public String getIdColumn() {
		return parameters.get(FastOrmKeys.idColumn);
	}

	@Override
	public String getIdType() {
		return parameters.get(FastOrmKeys.idType);
	}

	@Override
	public ISecondaryTempTableMaker getMaker() {
		return maker;
	}
}
