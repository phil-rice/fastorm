package org.fastorm.context;

import java.sql.Connection;
import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.maps.Maps;

public class ReadContext extends AbstractContext implements IGetDrainedTableForEntityDefn {

	private final Map<IEntityDefn, IDrainedTableData> map = Maps.newMap();

	public ReadContext(IFastOrmContainer fastOrm, Connection connection) {
		super(fastOrm, connection);
	}

	@Override
	public IDrainedTableData get(IEntityDefn entityDefn) {
		return map.get(entityDefn);
	}

	public void add(IDrainedTableData data) {
		map.put(data.getEntityDefn(), data);
	}
}
