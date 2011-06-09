package org.fastorm.dataSet.impl;

import java.text.MessageFormat;
import java.util.Map;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.maps.Maps;

public class GetDrainedTableForEntityDefnMock implements IGetDrainedTableForEntityDefn {

	private final Map<IEntityDefn, IDrainedTableData> map;

	public GetDrainedTableForEntityDefnMock(Object... entityDefnsAndDrainedTableData) {
		this.map = Maps.makeLinkedMap(entityDefnsAndDrainedTableData);
	}

	@Override
	public IDrainedTableData get(IEntityDefn entityDefn) {
		if (!map.containsKey(entityDefn))
			throw new IllegalArgumentException(MessageFormat.format(FastOrmMessages.illegalKey, entityDefn, map.keySet()));
		return map.get(entityDefn);
	}

}
