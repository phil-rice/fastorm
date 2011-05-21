package org.fastorm.temp.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.impl.FastOrm;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmOptions;
import org.fastorm.constants.FastOrmTestValues;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.impl.EntityDefn;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.IIntegrationTest;
import org.fastorm.utilities.Maps;
import org.fastorm.utilities.WrappedException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

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
			FastOrmKeys.idType, FastOrmTestValues.primaryIdType//
			);

	protected void emptyDatabase() {
		// if you get 'unknown database' error, you need to create the database fastorm. See the file MySqlTest.xml
		sqlHelper.dropAllTables();

	}

	@Override
	protected void setUp() throws Exception {
		XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("MySqlTest.xml", getClass()));
		dataSource = beanFactory.getBean(DataSource.class);
		jdbcTemplate = new JdbcTemplate(dataSource);
		sqlHelper = new SqlHelper(jdbcTemplate);
		fastOrm = new FastOrm().withDataSource(dataSource).//
				withOptions(FastOrmOptions.withOutTempTables()).//
				withEntityDefn(new EntityDefn(null, parameters, Collections.<IEntityDefn> emptyList())).getContainer();
		fastOrm5 = fastOrm.withBatchSize(5).getContainer();
	}

	protected void execute(final ICallback<OrmReadContext> callback) {
		jdbcTemplate.execute(new ConnectionCallback<Void>() {
			@Override
			public Void doInConnection(Connection con) throws SQLException, DataAccessException {
				try {
					callback.process(new OrmReadContext(con));
				} catch (Exception e) {
					throw WrappedException.wrap(e);
				}
				return null;
			}
		});
	};

	protected <T> T query(final IFunction1<OrmReadContext, T> fn) {
		T result = jdbcTemplate.execute(new ConnectionCallback<T>() {
			@Override
			public T doInConnection(Connection con) throws SQLException, DataAccessException {
				try {
					return fn.apply(new OrmReadContext(con));
				} catch (Exception e) {
					throw WrappedException.wrap(e);
				}
			}
		});
		return result;
	};
}
