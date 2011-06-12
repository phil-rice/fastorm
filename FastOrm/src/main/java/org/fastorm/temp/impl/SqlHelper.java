package org.fastorm.temp.impl;

import static org.fastorm.utilities.collections.Iterables.aggregate;
import static org.fastorm.utilities.collections.Iterables.map;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.fastorm.api.IJob;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMakerAndEntityDefnVisitor;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.utilities.strings.Strings;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

//import static org.fastorm.utilities.Iterables.*;
public class SqlHelper {
	private final JdbcTemplate template;

	public SqlHelper(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	public void execute(String sql) {
		template.execute(sql);
	}

	public void execute(String pattern, String... arguments) {
		for (String a : arguments)
			template.execute(MessageFormat.format(pattern, a));
	}

	public void execute(String pattern, List<String> arguments) {
		for (String a : arguments)
			template.execute(MessageFormat.format(pattern, a));
	}

	public <T> List<T> mapResultSet(ResultSet resultSet, IFunction1<ResultSet, T> fn) {
		List<T> result = new ArrayList<T>();
		try {
			while (resultSet.next())
				result.add(fn.apply(resultSet));
			return result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public <T> T evaluateMetadata(IFunction1<DatabaseMetaData, T> fn) {
		try {
			Connection c = template.getDataSource().getConnection();
			DatabaseMetaData md = c.getMetaData();
			try {
				T result = fn.apply(md);
				return result;
			} finally {
				c.close();
			}
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public Map<String, String> columns(final String tableName) {
		return evaluateMetadata(new IFunction1<DatabaseMetaData, Map<String, String>>() {
			@Override
			public Map<String, String> apply(DatabaseMetaData from) throws Exception {
				ResultSet rs = from.getColumns(null, null, tableName, null);
				Map<String, String> result = new HashMap<String, String>();
				while (rs.next()) {
					String key = rs.getString("COLUMN_NAME");
					String value = rs.getString("TYPE_NAME");
					result.put(key, value);
				}
				return result;
			}
		});
	}

	public int tableSize(String tableName) {
		return template.queryForInt("select count(*) from " + tableName);
	}

	public List<String> tables() {
		return evaluateMetadata(new IFunction1<DatabaseMetaData, List<String>>() {
			@Override
			public List<String> apply(DatabaseMetaData from) throws Exception {
				ResultSet rs = from.getTables(null, null, "%", null);
				return mapResultSet(rs, new IFunction1<ResultSet, String>() {
					@Override
					public String apply(ResultSet from) throws Exception {
						return from.getString(3);
					}
				});
			}
		});
	}

	public void create(String tableName, String... fieldNamesAndType) {
		Map<String, String> defn = Maps.<String, String> makeMap((Object[]) fieldNamesAndType);
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + tableName + "(");
		sql.append(aggregate(map(defn.entrySet(), Maps.entryToStr("{0} {1}")), Strings.strJoin(",")));
		sql.append(")");
		template.execute(sql.toString());
	}

	public void index(IJob job) {
		final Map<IEntityDefn, List<String>> columnsToIndex = Maps.newMap();
		IEntityDefn.Utils.walk(job.getContainer(), new IMakerAndEntityDefnVisitor() {
			@Override
			public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) throws Exception {
				Maps.addToList(columnsToIndex, primary, primary.getIdColumn());
			}

			@Override
			public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) throws Exception {
				maker.enrichListOfColumnsToIndex(columnsToIndex, parent, child);
			}
		});
		for (Entry<IEntityDefn, List<String>> entry : columnsToIndex.entrySet()) {
			for (String column : entry.getValue()) {
				String tableName = entry.getKey().getTableName();
				execute("alter table " + tableName + " add index " + tableName + "_" + column + "(" + column + ")");
			}
		}
	}

	public void insert(String tableName, Object... namesAndValues) {
		Map<Object, Object> data = Maps.makeMap(namesAndValues);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + tableName + " (");
		sql.append(aggregate(data.keySet(), Strings.join(",")));
		sql.append(") values (");
		sql.append(aggregate(map(data.keySet(), Maps.get(data)), Strings.join(",")));
		sql.append(")");
		template.execute(sql.toString());
	}

	public void insert(String tableName, int size, Object... namesAndPatterns) {
		insertWithOffset(tableName, size, 0, namesAndPatterns);
	}

	public void insertWithOffset(String tableName, int size, final int offset, Object... namesAndPatterns) {
		final Map<String, String> defn = Maps.<String, String> makeMap(namesAndPatterns);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + tableName + " (");
		sql.append(aggregate(defn.keySet(), Strings.strJoin(",")));
		sql.append(") values ");
		sql.append(aggregate(Iterables.<String> times(size, new IFunction1<Integer, String>() {
			@Override
			public String apply(final Integer index) throws Exception {
				String line = "(" + aggregate(map(defn.keySet(), Maps.keyToValuePatternToStr(defn, index + offset)), Strings.strJoin(",")) + ")";
				return line;
			}
		}), Strings.strJoin(",\n")));
		template.execute(sql.toString());
	}

	public void assertTableMatches(String tableName, int size, int offset, Object... namesAndPatterns) {
		final Map<String, String> data = Maps.makeMap(namesAndPatterns);
		final AtomicInteger count = new AtomicInteger(offset);
		template.query("select * from " + tableName, new ResultSetExtractor<Void>() {
			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					int index = count.getAndIncrement();
					Set<Entry<String, String>> entries = data.entrySet();
					Assert.assertEquals(data.size(), rs.getMetaData().getColumnCount());
					for (Entry<String, String> entry : entries) {
						Object actual = rs.getObject(entry.getKey());
						String expected = MessageFormat.format(entry.getValue(), entry.getKey(), index);
						Assert.assertEquals(expected, actual.toString());
					}
				}
				return null;
			}
		});
	}

	public void dropAllTables() {
		execute("drop table {0}", tables());
	}

	public void insert(Map<IEntityDefn, List<Map<String, Object>>> data) {
		for (Entry<IEntityDefn, List<Map<String, Object>>> entry : data.entrySet()) {
			String tableName = entry.getKey().getTableName();
			for (Map<String, Object> lineData : entry.getValue())
				insert(tableName, Maps.<Object> toArray(lineData, new Object[0]));
		}
	}

	public void index(IEntityDefn entityDefn) {
		template.execute("alter table " + entityDefn.getTableName() + " add index " + entityDefn.getTableName() + "_idx(" + entityDefn.getIdColumn() + ")");
	}
}
