package org.fastorm.oneToMany;

import java.util.Map;

import org.fastorm.api.IJobOptimisations;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.IContext;
import org.fastorm.defns.IEntityDefn;

public class OneToManyByStoredProcedures extends OneToMany implements IOneToMany {

	public OneToManyByStoredProcedures(IJobOptimisations optimisations) {
		super(optimisations);
	}

	@Override
	public boolean accept(Map<String, String> parent, Map<String, String> child) {
		return child.containsKey(FastOrmKeys.childLink);
	}

	@Override
	public void create(IContext context, IEntityDefn parent, IEntityDefn child) {
		if (!optimisations.optimiseLeafAccess() || child.getChildren().size() > 0)
			update(context, FastOrmStringTemplates.createOneToManyStoredProcedure, child, "procName", makeProcName(child),//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
		else
			update(context, FastOrmStringTemplates.createOneToManyLeafStoredProcedure, child, "procName", makeProcName(child),//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
		update(context, FastOrmStringTemplates.createOneToManyTempTable, child, FastOrmKeys.parentIdColumn, parent.getIdColumn(), FastOrmKeys.parentIdType, parent.getIdType());
		if (optimisations.indexSecondaryTables())
			update(context, FastOrmStringTemplates.addIndexToTempTable, child);
	}

	@Override
	public void clean(IContext context, IEntityDefn child) {
		update(context, FastOrmStringTemplates.dropTempTable, child);
	}

	@Override
	public int startOfBatch(IContext context, IEntityDefn parent, IEntityDefn child) {
		if (!optimisations.optimiseLeafAccess() || child.getChildren().size() > 0) {
			update(context, FastOrmStringTemplates.truncateTempTable, child);
			return update(context, FastOrmStringTemplates.populateOneToManyTempTable, child,//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
		} else
			return 0;
	}

	@Override
	public void drain(IContext context, IEntityDefn parent, IEntityDefn child) {
		if (!optimisations.optimiseLeafAccess() || child.getChildren().size() > 0)
			drainSecondary(context, child, FastOrmStringTemplates.drainSecondaryTable);
		else
			drainSecondary(context, child, FastOrmStringTemplates.drainLeafOneToManyTable,//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
	}

}
