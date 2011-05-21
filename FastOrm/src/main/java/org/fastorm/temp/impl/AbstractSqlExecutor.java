package org.fastorm.temp.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.impl.DrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.sqlDialects.ISqlStrings;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AbstractSqlExecutor {

	protected int updatePrimary(IFastOrmContainer fastOrm, OrmReadContext context, String template, Object... parameters) {
		IEntityDefn primary = fastOrm.getEntityDefn();
		String sql = fastOrm.getSqlStrings().getFromTemplate(fastOrm.getOptions(), template, primary.parameters(), parameters);
		return updateSql(fastOrm, context, sql);
	}

	protected int update(IFastOrmContainer fastOrm, OrmReadContext context, String template, IEntityDefn entityDefn, Object... parameters) {
		String sql = fastOrm.getSqlStrings().getFromTemplate(fastOrm.getOptions(), template, entityDefn.parameters(), parameters);
		return updateSql(fastOrm, context, sql);
	}

	protected IDrainedTableData drainPrimary(final IFastOrmContainer fastOrm, OrmReadContext context, String template, Object... parameters) {
		String sql = fastOrm.getSqlStrings().getFromTemplate(fastOrm.getOptions(), template, fastOrm.getEntityDefn().parameters(), parameters);
		IDrainedTableData result = querySql(fastOrm, context, sql, new ResultSetExtractor<IDrainedTableData>() {
			@Override
			public IDrainedTableData extractData(ResultSet rs) throws SQLException, DataAccessException {
				return new DrainedTableData(fastOrm.getMemoryManager(), fastOrm.getEntityDefn(), rs);
			}
		});
		return result;
	}

	protected IDrainedTableData drainSecondary(final IFastOrmContainer fastOrm, OrmReadContext context, final IEntityDefn childDefn, String template, Object... parameters) {
		ISqlStrings sqlStrings = fastOrm.getSqlStrings();
		String sql = sqlStrings.getFromTemplate(fastOrm.getOptions(), template, childDefn.parameters(), parameters);
		IDrainedTableData result = querySql(fastOrm, context, sql, new ResultSetExtractor<IDrainedTableData>() {
			@Override
			public IDrainedTableData extractData(ResultSet rs) throws SQLException, DataAccessException {
				return new DrainedTableData(fastOrm.getMemoryManager(), childDefn, rs);
			}
		});
		return result;
	}

	private IDrainedTableData querySql(final IFastOrmContainer fastOrm, OrmReadContext context, String sql, final ResultSetExtractor<IDrainedTableData> rse) {
		final AtomicLong queryTime = new AtomicLong();
		long startTime = System.nanoTime();
		IDrainedTableData result = fastOrm.getJdbcTemplate().query(sql, new ResultSetExtractor<IDrainedTableData>() {
			@Override
			public IDrainedTableData extractData(ResultSet rs) throws SQLException, DataAccessException {
				queryTime.set(System.nanoTime());
				return rse.extractData(rs);
			}
		});
		fastOrm.getSqlLogger().queried(System.nanoTime() - startTime, (queryTime.get() - startTime), sql, result.size());
		return result;
	}

	private int updateSql(IFastOrmContainer fastOrm, OrmReadContext context, String sql) {
		long startTime = System.nanoTime();
		int result = fastOrm.getJdbcTemplate().update(sql);
		fastOrm.getSqlLogger().updated(System.nanoTime() - startTime, sql, result);
		return result;
	}

}
