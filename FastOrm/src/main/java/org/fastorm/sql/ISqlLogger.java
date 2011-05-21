package org.fastorm.sql;


public interface ISqlLogger {
	void updated(long duration, String sql, int columnsAffected);

	void queried(long duration, long sqlDuration, String sql, int columnsAffected);

	void total(Long t);
}
