package org.fastorm.reader.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;

public class OrmReadContext implements IGetDrainedTableForEntityDefn {

	private final Connection connection;
	private final Map<IEntityDefn, IDrainedTableData> map = Maps.newMap();

	public OrmReadContext(Connection connection) {
		this.connection = connection;
	}

	public int update(IFunction1<Statement, Integer> callback) throws SQLException {
		try {
			Statement statement = connection.createStatement();
			try {
				return callback.apply(statement);
			} finally {
				statement.close();
			}
		} catch (SQLException e) {
			throw e;// these get picked up by spring, which turns them into much nicer exceptions
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public IDrainedTableData get(IEntityDefn entityDefn) {
		return map.get(entityDefn);
	}

	public void add(IDrainedTableData data) {
		map.put(data.getEntityDefn(), data);
	}
}
