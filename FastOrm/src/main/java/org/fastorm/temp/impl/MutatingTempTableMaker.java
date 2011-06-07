package org.fastorm.temp.impl;

import java.sql.SQLException;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.OrmWriteContext;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.IMutatingTempTableMaker;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.writer.impl.IMutatedDataTable;

public class MutatingTempTableMaker extends AbstractSqlExecutor implements IMutatingTempTableMaker {

	@Override
	public void create(OrmWriteContext writeContext, IEntityDefn entityDefn) {
		executeTemplate(writeContext, entityDefn, FastOrmStringTemplates.createUpdateTempTable);
	}

	@Override
	public int populateUpdate(OrmWriteContext writeContext, IMutatedDataTable table) {
		try {
			IEntityDefn entityDefn = table.getEntityDefn();
			String tableName = entityDefn.getTableName(); // This is wrong table name...Need one for updates and deletes...
			return bulkInsert(writeContext, tableName, table.changedColumnIndicies(), table);
		} catch (SQLException e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public void update(OrmWriteContext writeContext, IEntityDefn entityDefn) {
		executeTemplate(writeContext, entityDefn, FastOrmStringTemplates.update);
	}

	private void executeTemplate(OrmWriteContext writeContext, IEntityDefn entityDefn, String template) {
		try {
			IFastOrmContainer fastOrm = writeContext.getFastOrm();
			ISqlStrings sqlStrings = fastOrm.getSqlStrings();
			FastOrmOptions options = fastOrm.getOptions();
			String sql = sqlStrings.getFromTemplate(options, template, entityDefn.parameters());
			writeContext.update(sql);
		} catch (SQLException e) {
			throw WrappedException.wrap(e);
		}
	}

}
