package org.fastorm.api;

import org.fastorm.api.impl.JobServices;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.sql.ISqlLogger;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.ITempTableMakerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public interface IFastOrmContainer extends IJob {
	IMemoryManager getMemoryManager();

	ISqlStrings getSqlStrings();

	ISqlLogger getSqlLogger();

	JdbcTemplate getJdbcTemplate();

	JobServices getServices();

	ITempTableMakerFactory getTempTableMakerFactory();

	IEntityReaderThin getEntityReaderThin();

	void shutdown();

}
