package org.fastorm.context;

import java.sql.SQLException;
import java.sql.Statement;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.utilities.functions.IFunction1;
import org.springframework.jdbc.core.ResultSetExtractor;

public interface IContext {
	int update(IFunction1<Statement, Integer> callback) throws SQLException;

	int update(String sql) throws SQLException;

	<T> T query(String sql, ResultSetExtractor<T> extractor) throws SQLException;

	IFastOrmContainer getFastOrm();

}
