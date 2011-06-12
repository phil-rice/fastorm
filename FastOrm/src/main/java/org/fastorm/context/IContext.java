package org.fastorm.context;

import java.sql.SQLException;
import java.sql.Statement;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.utilities.functions.IFunction1;
import org.springframework.jdbc.core.ResultSetExtractor;

public interface IContext extends IGetDrainedTableForEntityDefn {
	int update(IFunction1<Statement, Integer> callback) throws SQLException;

	int update(String sql) throws SQLException;

	<T> T query(String sql, ResultSetExtractor<T> extractor) throws SQLException;

	IFastOrmContainer getFastOrm();

	void add(IDrainedTableData result);

	IMutableDataSet buildDataSet();

}
