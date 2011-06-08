package org.fastorm.temp.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmConstants;
import org.fastorm.context.IContext;
import org.fastorm.context.ReadContext;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.impl.DrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.utilities.maps.IListOfSimpleMapWithIndex;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AbstractSqlExecutor {

	protected int updatePrimary(IFastOrmContainer fastOrm, ReadContext context, String template, Object... parameters) {
		IEntityDefn primary = fastOrm.getEntityDefn();
		String sql = fastOrm.getSqlStrings().getFromTemplate(fastOrm, template, primary.parameters(), parameters);
		return updateSql(fastOrm, context, sql);
	}

	protected int update(IFastOrmContainer fastOrm, ReadContext context, String template, IEntityDefn entityDefn, Object... parameters) {
		String sql = fastOrm.getSqlStrings().getFromTemplate(fastOrm, template, entityDefn.parameters(), parameters);
		return updateSql(fastOrm, context, sql);
	}

	protected void drainPrimary(final IFastOrmContainer fastOrm, final ReadContext context, String template, Object... parameters) {
		final IEntityDefn entityDefn = fastOrm.getEntityDefn();
		String sql = fastOrm.getSqlStrings().getFromTemplate(fastOrm, template, entityDefn.parameters(), parameters);
		IDrainedTableData result = querySql(fastOrm, context, sql, new ResultSetExtractor<IDrainedTableData>() {
			@Override
			public IDrainedTableData extractData(ResultSet rs) throws SQLException, DataAccessException {
				IMemoryManager memoryManager = fastOrm.getMemoryManager();
				DrainedTableData tableData = memoryManager.makeDrainedTableData(memoryManager, entityDefn, context, rs);
				return tableData;
			}
		});
		context.add(result);
	}

	protected void drainSecondary(final IFastOrmContainer fastOrm, final ReadContext context, final IEntityDefn childDefn, String template, Object... parameters) {
		ISqlStrings sqlStrings = fastOrm.getSqlStrings();
		String sql = sqlStrings.getFromTemplate(fastOrm, template, childDefn.parameters(), parameters);
		IDrainedTableData result = querySql(fastOrm, context, sql, new ResultSetExtractor<IDrainedTableData>() {
			@Override
			public IDrainedTableData extractData(ResultSet rs) throws SQLException, DataAccessException {
				IMemoryManager memoryManager = fastOrm.getMemoryManager();
				DrainedTableData tableData = memoryManager.makeDrainedTableData(memoryManager, childDefn, context, rs);
				return tableData;
			}
		});
		context.add(result);
	}

	protected int bulkInsert(IContext context, String tableName, List<Integer> changedColumns, IListOfSimpleMapWithIndex<String, Object> table) throws SQLException {
		IFastOrmContainer fastOrm = context.getFastOrm();
		ISqlStrings sqlStrings = fastOrm.getSqlStrings();
		String sql = sqlStrings.buildInsert(tableName, table, changedColumns);
		return updateSql(fastOrm, context, sql);
	}

	private IDrainedTableData querySql(final IFastOrmContainer fastOrm, ReadContext context, String sql, final ResultSetExtractor<IDrainedTableData> rse) {
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

	private int updateSql(IFastOrmContainer fastOrm, IContext context, String sql) {
		long startTime = System.nanoTime();
		int result = fastOrm.getJdbcTemplate().update(sql);
		fastOrm.getSqlLogger().updated(System.nanoTime() - startTime, sql, result);
		return result;
	}

	protected String makeProcName(IEntityDefn entityDefn) {
		return FastOrmConstants.procNamePrefix + entityDefn.getEntityName();
	}

	public Object makeProcName(IEntityDefn entityDefn, String postFix) {
		return "proc_" + entityDefn.getEntityName() + postFix;
	}

}
