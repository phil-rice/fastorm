package org.fastorm.temp.impl;

import java.sql.SQLException;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.IContext;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.IMutatingTempTableMaker;
import org.fastorm.utilities.exceptions.WrappedException;

public class MutatingTempTableMaker extends AbstractSqlExecutor implements IMutatingTempTableMaker {

	@Override
	public void create(IContext writeContext, IEntityDefn entityDefn) {
		executeTemplate(writeContext, entityDefn, FastOrmStringTemplates.createUpdateTempTable);
	}

	@Override
	public int populateUpdate(IContext writeContext, IDrainedTableData table) {
		try {
			IEntityDefn entityDefn = table.getEntityDefn();
			String tableName = entityDefn.getTableName(); // This is wrong table name...Need one for updates and deletes...
			return bulkInsert(writeContext, tableName, table.changedColumnIndicies(), table);
		} catch (SQLException e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public void update(IContext context, IEntityDefn entityDefn) {
		executeTemplate(context, entityDefn, FastOrmStringTemplates.update);
	}

	private void executeTemplate(IContext context, IEntityDefn entityDefn, String template) {
		try {
			IFastOrmContainer fastOrm = context.getFastOrm();
			ISqlStrings sqlStrings = fastOrm.getSqlStrings();
			String sql = sqlStrings.getFromTemplate(template, context.getFastOrm().getOptimisations(), entityDefn.parameters());
			context.update(sql);
		} catch (SQLException e) {
			throw WrappedException.wrap(e);
		}
	}

}
