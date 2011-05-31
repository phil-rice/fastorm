package org.fastorm.reader.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;
import org.springframework.jdbc.core.ResultSetExtractor;

public class OrmReadContext {

	private final Connection connection;

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

	public <T> void query(ResultSetExtractor<T> rse) {

	}
}
