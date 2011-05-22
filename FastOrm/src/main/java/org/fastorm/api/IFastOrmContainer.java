package org.fastorm.api;

import org.fastorm.api.impl.FastOrmServices;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.sql.ISqlLogger;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ITempTableMakerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public interface IFastOrmContainer extends IFastOrm {
	IMemoryManager getMemoryManager();

	ISqlStrings getSqlStrings();

	ISqlLogger getSqlLogger();

	JdbcTemplate getJdbcTemplate();

	FastOrmOptions getOptions();

	IPrimaryTempTableMaker getPrimaryTempTableMaker();

	FastOrmServices getServices();

	ITempTableMakerFactory getTempTableMakerFactory();

	IEntityReaderThin getEntityReaderThin();

	void shutdown();
}
