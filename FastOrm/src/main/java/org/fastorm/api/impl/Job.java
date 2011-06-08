package org.fastorm.api.impl;

import java.text.MessageFormat;
import java.util.Map;

import javax.sql.DataSource;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.IJob;
import org.fastorm.constants.FastOrmMessages;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.memory.MemoryManager;
import org.fastorm.mutate.IMutableItem;
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
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.ISimpleMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Job extends JobDetails implements IFastOrmContainer {
	private JobServices services = new JobServices();
	private final IEntityReaderThin entityReaderThin;
	private final ISqlStrings sqlStrings;
	private final ITempTableMakerFactory tempTableMakerFactory;
	private final JdbcTemplate jdbcTemplate;
	private final IMemoryManager memoryManager;

	private final ISqlLogger sqlLogger;

	public Job() {
		this(null, null);
	}

	public Job(JobServices services, IEntityReaderThin entityReaderThin, ISqlStrings sqlStrings, //
			IEntityDefn entityDefn, ITempTableMakerFactory tempTableMakerFactory, JdbcTemplate jdbcTemplate, //
			IPrimaryTempTableMaker primaryTempTableMaker, IMemoryManager memoryManager, ISqlLogger sqlLogger, //
			final boolean indexPrimaryTables, boolean indexSecondaryTables, boolean useTemporaryTables, boolean createAnddropProceduresAtStartOfRun,//
			boolean optimiseLeafAccess, int batchSize, int maxForOneThread, int byteBufferSize) {
		super(entityDefn, primaryTempTableMaker, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
		this.services = services;
		this.entityReaderThin = entityReaderThin;
		this.sqlStrings = sqlStrings;
		this.tempTableMakerFactory = tempTableMakerFactory;
		this.jdbcTemplate = jdbcTemplate;
		this.memoryManager = memoryManager.withJobDetails(this);
		this.sqlLogger = sqlLogger;
	}

	public Job(IEntityDefn entityDefn, DataSource dataSource) {
		this(new JobServices(), new EntityReaderThin(),//
				new SqlStrings(new ClassPathResource("MySql.st")), entityDefn, new TempTableMakerFactory(), //
				dataSource == null ? null : new JdbcTemplate(dataSource),//
				new AllEntitiesTempTableMaker(), new MemoryManager(), new NoSqlLogger(), //
				true, true, true, true, true, 100, 1000, 1000000);
	}

	@Override
	public IJob withServices(JobServices services) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withThinInterface(IEntityReaderThin entityReaderThin) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withSqlDialect(ISqlStrings sqlStrings) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withEntityDefn(IEntityDefn entityDefn) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withTempTableMaker(ISecondaryTempTableMaker intermediateTempTableMaker) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withMemoryManager(IMemoryManager memoryManager) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withDataSource(DataSource dataSource) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, new JdbcTemplate(dataSource), primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withMaxForOneThread(int maxForOneThread) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withBatchSize(int batchSize) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withSqlLogger(ISqlLogger sqlLogger) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withTempTables(boolean useTemporaryTables) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withByteBufferSize(int byteBufferSize) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withIndexSecondaryTables(boolean indexSecondaryTables) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withMaxInOneThread(int maxForOneThread) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withCreateAndDropProceduresAtStart(boolean createAnddropProceduresAtStartOfRun) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IJob withOptimiseLeafAccess(boolean optimiseLeafAccess) {
		return new Job(services, entityReaderThin, sqlStrings, entityDefn, tempTableMakerFactory, jdbcTemplate, primaryTempTableMaker, memoryManager, sqlLogger, indexPrimaryTables, indexSecondaryTables, useTemporaryTables, createAnddropProceduresAtStartOfRun, optimiseLeafAccess, batchSize, maxForOneThread, byteBufferSize);
	}

	@Override
	public IEntityReader<ISimpleMap<String, Object>> makeReader() {
		checkSetup();
		return new EntityReader<ISimpleMap<String, Object>>(this);
	}

	@Override
	public <T> IMutate<T> makeMutator() {
		checkSetup();
		return new IMutate<T>() {

			@Override
			public void mutate(IFunction1<T, T> mutateFunction) {

			}

			@Override
			public void mutateMap(IFunction1<Map<String, Object>, Map<String, Object>> mutateFunction) {

			}

			@Override
			public void readModifyWrite(IPrimaryTempTableMaker maker, ICallback<IMutableDataSet> callback) {

			}

			@Override
			public void readModifyWrite(AllEntitiesTempTableMaker maker, ICallback<IMutableItem> iCallback) {

			}

		};
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
	public JobServices getServices() {
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((entityDefn == null) ? 0 : entityDefn.hashCode());
		result = prime * result + ((entityReaderThin == null) ? 0 : entityReaderThin.hashCode());
		result = prime * result + ((jdbcTemplate == null) ? 0 : jdbcTemplate.hashCode());
		result = prime * result + ((memoryManager == null) ? 0 : memoryManager.hashCode());
		result = prime * result + ((primaryTempTableMaker == null) ? 0 : primaryTempTableMaker.hashCode());
		result = prime * result + ((services == null) ? 0 : services.hashCode());
		result = prime * result + ((sqlLogger == null) ? 0 : sqlLogger.hashCode());
		result = prime * result + ((sqlStrings == null) ? 0 : sqlStrings.hashCode());
		result = prime * result + ((tempTableMakerFactory == null) ? 0 : tempTableMakerFactory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (entityDefn == null) {
			if (other.entityDefn != null)
				return false;
		} else if (!entityDefn.equals(other.entityDefn))
			return false;
		if (entityReaderThin == null) {
			if (other.entityReaderThin != null)
				return false;
		} else if (!entityReaderThin.equals(other.entityReaderThin))
			return false;
		if (jdbcTemplate == null) {
			if (other.jdbcTemplate != null)
				return false;
		} else if (!jdbcTemplate.equals(other.jdbcTemplate))
			return false;
		if (memoryManager == null) {
			if (other.memoryManager != null)
				return false;
		} else if (!memoryManager.equals(other.memoryManager))
			return false;
		if (primaryTempTableMaker == null) {
			if (other.primaryTempTableMaker != null)
				return false;
		} else if (!primaryTempTableMaker.equals(other.primaryTempTableMaker))
			return false;
		if (services == null) {
			if (other.services != null)
				return false;
		} else if (!services.equals(other.services))
			return false;
		if (sqlLogger == null) {
			if (other.sqlLogger != null)
				return false;
		} else if (!sqlLogger.equals(other.sqlLogger))
			return false;
		if (sqlStrings == null) {
			if (other.sqlStrings != null)
				return false;
		} else if (!sqlStrings.equals(other.sqlStrings))
			return false;
		if (tempTableMakerFactory == null) {
			if (other.tempTableMakerFactory != null)
				return false;
		} else if (!tempTableMakerFactory.equals(other.tempTableMakerFactory))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Job [services=" + services + ", entityReaderThin=" + entityReaderThin + ", sqlStrings=" + sqlStrings + ", entityDefn=" + entityDefn + ", tempTableMakerFactory=" + tempTableMakerFactory + ", jdbcTemplate=" + jdbcTemplate + ", primaryTempTableMaker=" + primaryTempTableMaker + ", memoryManager=" + memoryManager + ", sqlLogger=" + sqlLogger + "]";
	}

}
