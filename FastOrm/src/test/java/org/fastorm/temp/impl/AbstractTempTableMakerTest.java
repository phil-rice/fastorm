package org.fastorm.temp.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.impl.Job;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmTestValues;
import org.fastorm.context.Context;
import org.fastorm.context.IContext;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.impl.EntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.annotations.IntegrationTest;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.utilities.tests.IIntegrationTest;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

@IntegrationTest
public abstract class AbstractTempTableMakerTest extends TestCase implements IIntegrationTest {

	protected DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;

	protected SqlHelper sqlHelper;
	protected IFastOrmContainer fastOrm;
	protected IFastOrmContainer fastOrm5;
	protected final Map<String, String> parameters = Maps.<String, String> makeMap(//
			FastOrmKeys.tableName, FastOrmTestValues.primaryTableName, //
			FastOrmKeys.tempTableName, FastOrmTestValues.primaryTempTableName,//
			FastOrmKeys.idColumn, FastOrmTestValues.primaryIdColumn,//
			FastOrmKeys.idType, FastOrmTestValues.primaryIdType,//
			FastOrmKeys.maxLinesPerBatch, FastOrmTestValues.primaryMaxLinesPerBatch);

	protected void emptyDatabase() {
		// if you get 'unknown database' error, you need to create the database fastorm. See the file MySqlTest.xml
		sqlHelper.dropAllTables();
	}

	@Override
	protected void setUp() throws Exception {
		XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("MySqlTest.xml", getClass()));
		dataSource = beanFactory.getBean(DataSource.class);
		jdbcTemplate = new JdbcTemplate(dataSource);
		sqlHelper = new SqlHelper(dataSource);
		fastOrm = new Job().withDataSource(dataSource).//
				withEntityDefn(new EntityDefn(null, parameters, makeChildEntities())).getContainer();
		fastOrm5 = fastOrm.withBatchSize(5).getContainer();
	}

	protected List<IEntityDefn> makeChildEntities() {
		return Collections.<IEntityDefn> emptyList();
	}

	protected void execute(final ICallback<IContext> callback) {
		jdbcTemplate.execute(new ConnectionCallback<Void>() {
			@Override
			public Void doInConnection(Connection con) throws SQLException, DataAccessException {
				try {
					callback.process(new Context(fastOrm5, con));
				} catch (Exception e) {
					throw WrappedException.wrap(e);
				}
				return null;
			}
		});
	};

	protected <T> T query(final IFunction1<IContext, T> fn) {
		T result = jdbcTemplate.execute(new ConnectionCallback<T>() {
			@Override
			public T doInConnection(Connection con) throws SQLException, DataAccessException {
				try {
					T result = fn.apply(new Context(fastOrm5, con));
					if (result == null)
						throw new NullPointerException();
					return result;
				} catch (Exception e) {
					throw WrappedException.wrap(e);
				}
			}
		});
		return result;
	}

	protected IGetDrainedTableForEntityDefn getTheTables(final IFastOrmContainer fastOrm, final ISecondaryTempTableMaker maker, final IEntityDefn child, final int page) {
		IGetDrainedTableForEntityDefn getter = query(new IFunction1<IContext, IGetDrainedTableForEntityDefn>() {
			@Override
			public IGetDrainedTableForEntityDefn apply(IContext context) throws Exception {
				IPrimaryTempTableMaker primaryMaker = fastOrm.getPrimaryTempTableMaker();
				IEntityDefn primary = fastOrm.getEntityDefn();
				primaryMaker.drop(context);
				primaryMaker.dropStoredProcedure(context);
				primaryMaker.create(context);
				primaryMaker.populate(context, page);
				primaryMaker.drain(context);

				maker.drop(context, child);
				maker.dropStoredProcedure(context, primary, child);
				maker.create(context, primary, child);
				maker.populate(context, primary, child);
				maker.drain(context, primary, child);
				return context;
			}
		});
		return getter;
	};
}
