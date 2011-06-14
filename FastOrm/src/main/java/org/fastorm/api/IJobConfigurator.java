package org.fastorm.api;

import javax.sql.DataSource;

import org.fastorm.api.impl.JobServices;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.sql.ISqlLogger;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.ITempTableMakerFactory;

public interface IJobConfigurator {

	IJob withServices(JobServices optimisation);

	IJob withDataSource(DataSource dataSource);

	IJob withMemoryManager(IMemoryManager memoryManager);

	IJob withSqlLogger(ISqlLogger logger);

	IJob withThinInterface(IEntityReaderThin entityReaderThin);

	IJob withSqlDialect(ISqlStrings sqlStrings);

	IJob withEntityDefn(IEntityDefn entityDefn);

	IJob withTempTableMaker(ITempTableMakerFactory tempTableMaker);

	IJob withMaxForOneThread(int max);

	IJob withBatchSize(int size);

	IJob withMaxInOneThread(int maxForOneThread);

	IJob withCreateAndDropAtStart(boolean createAndDropAtStart);

	IJob withOptimisations(IJobOptimisations optimisations);

}
