package org.fastorm.api;

import javax.sql.DataSource;

import org.fastorm.api.impl.JobServices;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.sql.ISqlLogger;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.ITempTableMakerFactory;

public interface IFastOrmContainer extends IJob {
	IMemoryManager getMemoryManager();

	ISqlStrings getSqlStrings();

	ISqlLogger getSqlLogger();

	DataSource getDataSource();

	JobServices getServices();

	ITempTableMakerFactory getTempTableMakerFactory();

	IEntityReaderThin getEntityReaderThin();

	void shutdown();

}
