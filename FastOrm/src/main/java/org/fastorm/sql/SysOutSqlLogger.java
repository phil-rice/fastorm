package org.fastorm.sql;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.utilities.strings.Strings;

public class SysOutSqlLogger implements ISqlLogger {

	@Override
	public void updated(long duration, String sql, int columnsAffected) {
		System.out.println(String.format(FastOrmMessages.sqlLoggerUpdated, duration / 1000000.0, columnsAffected, Strings.oneLine(sql)));
	}

	@Override
	public void queried(long duration, long sqlDuration, String sql, int columnsAffected) {
		System.out.println(String.format(FastOrmMessages.sqlLoggerQueried, duration / 1000000.0, sqlDuration / 1000000.0, columnsAffected, Strings.oneLine(sql)));
	}

	@Override
	public void total(Long duration) {
		System.out.println(String.format(FastOrmMessages.totalLoggerTime, duration / 1000000.0));
	}
}
