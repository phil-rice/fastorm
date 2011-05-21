package org.fastorm.sql;

public class NoSqlLogger implements ISqlLogger {

	@Override
	public void updated(long duration, String sql, int columnsAffected) {
	}

	@Override
	public void queried(long duration, long sqlDuration, String sql, int columnsAffected) {
	}

	@Override
	public void total(Long t) {
	}

}
