package org.fastorm.api;

import javax.sql.DataSource;

import org.fastorm.api.impl.FastOrmServices;
import org.fastorm.constants.FastOrmOptions;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.sql.ISqlLogger;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.ISecondaryTempTableMaker;

public interface IFastOrmFactoryConfigurator {

	IFastOrm withOptimisation(FastOrmServices optimisation);

	IFastOrm withDataSource(DataSource dataSource);

	IFastOrm withMemoryManager(IMemoryManager memoryManager);

	IFastOrm withOptions(FastOrmOptions options);

	IFastOrm withSqlLogger(ISqlLogger logger);

	IFastOrm withThinInterface(IEntityReaderThin entityReaderThin);

	IFastOrm withSqlDialect(ISqlStrings sqlStrings);

	IFastOrm withEntityDefn(IEntityDefn entityDefn);

	IFastOrm withTempTableMaker(ISecondaryTempTableMaker intermediateTempTableMaker);

	IFastOrm withMaxForOneThread(int max);

	IFastOrm withBatchSize(int size);
}
