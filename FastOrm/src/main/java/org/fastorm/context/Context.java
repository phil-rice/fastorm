package org.fastorm.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.IJob;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.dataSet.impl.DataSetBuilder;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMakerAndEntityDefnFoldVisitor;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;
import org.springframework.jdbc.core.ResultSetExtractor;

public class Context implements IContext {
	private final Connection connection;
	private final IFastOrmContainer fastOrm;
	private final Map<IEntityDefn, IDrainedTableData> map = Maps.newMap();

	public Context(IJob fastOrm, Connection connection) {
		this.fastOrm = fastOrm.getContainer();
		this.connection = connection;
	}

	@Override
	public IFastOrmContainer getFastOrm() {
		return fastOrm;
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
	public int update(String sql) throws SQLException {
		try {
			Statement statement = connection.createStatement();
			try {
				return statement.executeUpdate(sql);
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
	public <T> T query(String sql, ResultSetExtractor<T> extractor) throws SQLException {
		try {
			Statement statement = connection.createStatement();
			try {
				ResultSet resultSet = statement.executeQuery(sql);
				T result = extractor.extractData(resultSet);
				return result;
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

	@Override
	public IMutableDataSet buildDataSet() {
		ICallback<Long> total = new ICallback<Long>() {
			@Override
			public void process(Long t) throws Exception {
				fastOrm.getSqlLogger().total(t);
			}
		};
		IMutableDataSet dataSet = IEntityDefn.Utils.aggregateAndTime(fastOrm, new IMakerAndEntityDefnFoldVisitor<IDrainedTableData, IMutableDataSet>() {
			@Override
			public IDrainedTableData acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
				return get(primary);
			}

			@Override
			public IDrainedTableData acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
				return get(child);
			}
		}, new DataSetBuilder(), total);
		return dataSet;
	}

}
