package org.fastorm.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AbstractContext implements IContext {
	private final Connection connection;
	private final IFastOrmContainer fastOrm;

	public AbstractContext(IFastOrmContainer fastOrm, Connection connection) {
		this.fastOrm = fastOrm;
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

}
