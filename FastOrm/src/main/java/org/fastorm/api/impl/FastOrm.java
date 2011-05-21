package org.fastorm.api.impl;

import java.text.MessageFormat;

import javax.sql.DataSource;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmMessages;
import org.fastorm.constants.FastOrmOptions;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.memory.NoMemoryManager;
import org.fastorm.mutate.IMutate;
import org.fastorm.reader.IEntityReader;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.reader.impl.EntityReader;
import org.fastorm.reader.impl.EntityReaderThin;
import org.fastorm.sql.ISqlLogger;
import org.fastorm.sql.NoSqlLogger;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.sqlDialects.SqlStrings;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.temp.ITempTableMakerFactory;
import org.fastorm.temp.impl.AllEntitiesTempTableMaker;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.ISimpleMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

public class FastOrm implements IFastOrmContainer {
	private FastOrmServices services = new FastOrmServices();
	private final IEntityReaderThin entityReaderThin;
	private final ISqlStrings sqlStrings;
	private final IEntityDefn entityDefn;
	private final ITempTableMakerFactory tempTableMakerFactory;
	private final JdbcTemplate jdbcTemplate;
	private final IPrimaryTempTableMaker primaryTempTableMaker;
	private final IMemoryManager memoryManager;
	private final FastOrmOptions options;

	private final ISqlLogger sqlLogger;

	public FastOrm() {
		this(null, null);
	}

	public FastOrm(FastOrmServices optimisation, IEntityReaderThin entityReaderThin, ISqlStrings sqlStrings, //
			IEntityDefn entityDefn, ITempTableMakerFactory tempTableMakerFactory, JdbcTemplate jdbcTemplate, //
			IPrimaryTempTableMaker primaryTempTableMaker, IMemoryManager memoryManager, ISqlLogger sqlLogger, FastOrmOptions options) {
		this.services = optimisation;
		this.entityReaderThin = entityReaderThin;
		this.sqlStrings = sqlStrings;
		this.entityDefn = entityDefn;
		this.tempTableMakerFactory = tempTableMakerFactory;
		this.jdbcTemplate = jdbcTemplate;
		this.primaryTempTableMaker = primaryTempTableMaker;
		this.memoryManager = memoryManager;
		this.sqlLogger = sqlLogger;
		this.options = options;
	}

	public FastOrm(IEntityDefn entityDefn, DataSource dataSource) {
		this(new FastOrmServices(), new EntityReaderThin(),//
				new SqlStrings(new ClassPathResource("MySql.st")), entityDefn, new TempTableMakerFactory(), //
				dataSource == null ? null : new JdbcTemplate(dataSource),//
				new AllEntitiesTempTableMaker(), new NoMemoryManager(), new NoSqlLogger(), new FastOrmOptions());
	}

	@Override
	public IFastOrm withOptimisation(FastOrmServices optimisation) {
		return new FastOrm(optimisation, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withThinInterface(IEntityReaderThin entityReaderThin) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withSqlDialect(ISqlStrings sqlStrings) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withEntityDefn(IEntityDefn entityDefn) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withTempTableMaker(ISecondaryTempTableMaker intermediateTempTableMaker) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withMemoryManager(IMemoryManager memoryManager) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withDataSource(DataSource dataSource) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, new JdbcTemplate(dataSource), primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withOptions(FastOrmOptions options) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withMaxForOneThread(int maxForOneThread) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withDataSize(int dataSetSize) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IFastOrm withSqlLogger(ISqlLogger sqlLogger) {
		return new FastOrm(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, options);
	}

	@Override
	public IEntityReader<ISimpleMap<String, Object>> makeReader() {
		checkSetup();
		return new EntityReader<ISimpleMap<String, Object>>(this);
	}

	@Override
	public <T> IMutate<T> makeMutator() {
		checkSetup();
		return null;
	}

	private void checkSetup() {
		checkNotNull("EntityReaderThin", entityReaderThin);
		checkNotNull("sqlDialect", sqlStrings);
		checkNotNull("entityDefn", entityDefn);
		checkNotNull("tempTableMakerFactory", tempTableMakerFactory);
		checkNotNull("primaryTempTableMaker", primaryTempTableMaker);
		checkNotNull("optimisation", services);
	}

	private void checkNotNull(String string, Object object) {
		if (object == null)
			throw new IllegalStateException(MessageFormat.format(FastOrmMessages.fastOrmFactoryIsNotConfiguredWith, string));

	}

	@Override
	public FastOrmServices getServices() {
		return services;
	}

	@Override
	public IEntityReaderThin getEntityReaderThin() {
		return entityReaderThin;
	}

	@Override
	public ISqlStrings getSqlStrings() {
		return sqlStrings;
	}

	@Override
	public IEntityDefn getEntityDefn() {
		return entityDefn;
	}

	@Override
	public ITempTableMakerFactory getTempTableMakerFactory() {
		return tempTableMakerFactory;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public IPrimaryTempTableMaker getPrimaryTempTableMaker() {
		return primaryTempTableMaker;
	}

	@Override
	public IMemoryManager getMemoryManager() {
		return memoryManager;
	}

	@Override
	public int getMaxForOneThread() {
		return options.maxForOneThread;
	}

	@Override
	public int getBatchSize() {
		return options.batchSize;
	}

	@Override
	public IFastOrmContainer getContainer() {
		return this;
	}

	@Override
	public void shutdown() {
		services.shutdown();
	}

	@Override
	public ISqlLogger getSqlLogger() {
		return sqlLogger;
	}

	@Override
	public FastOrmOptions getOptions() {
		return options;
	}
}
