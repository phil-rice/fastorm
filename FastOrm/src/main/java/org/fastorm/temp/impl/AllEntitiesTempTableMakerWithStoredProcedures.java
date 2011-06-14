package org.fastorm.temp.impl;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmConstants;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.IContext;

public class AllEntitiesTempTableMakerWithStoredProcedures extends AllEntitiesTempTableMaker {

	@Override
	public void clean(IContext context) {
		super.clean(context);
		updatePrimary(context, FastOrmStringTemplates.dropStoredProcedure, "procName", super.makeProcName(context.getFastOrm().getEntityDefn(), FastOrmConstants.allEntitiesPostfix));
	}

	@Override
	public void create(IContext context) {
		super.clean(context);
		updatePrimary(context, FastOrmStringTemplates.createAllEntitiesStoredProcedure, FastOrmKeys.procName, myProcName(context));
	}

	@Override
	public int startOfBatch(IContext context, int page) {
		IFastOrmContainer fastOrm = context.getFastOrm();
		int size = fastOrm.getBatchSize();
		int start = size * page;
		return updatePrimary(context, FastOrmStringTemplates.populateAllEntitiesTempTable, FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	@Override
	public void drain(IContext context, int page) {
		IFastOrmContainer fastOrm = context.getFastOrm();
		int size = fastOrm.getBatchSize();
		int start = size * page;
		drainPrimary(context, FastOrmStringTemplates.drainFromStoredProcedureWithStartAndSize,//
				FastOrmKeys.procName, myProcName(context), FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	private Object myProcName(IContext context) {
		return makeProcName(context.getFastOrm().getEntityDefn(), FastOrmConstants.allEntitiesPostfix);
	}

}
