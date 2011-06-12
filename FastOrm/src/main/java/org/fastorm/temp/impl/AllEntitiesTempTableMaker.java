package org.fastorm.temp.impl;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmConstants;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.IContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.utilities.maps.Maps;

public class AllEntitiesTempTableMaker extends AbstractSqlExecutor implements IPrimaryTempTableMaker {

	private boolean indexPrimaryTables;

	@Override
	public void drop(IContext context) {
		updatePrimary(context, FastOrmStringTemplates.dropTempTable);
	}

	@Override
	public void create(IContext context) {
		updatePrimary(context, FastOrmStringTemplates.createAllEntitiesTempTable);
		if (indexPrimaryTables)
			updatePrimary(context, FastOrmStringTemplates.addIndexToTempTable);
	}

	@Override
	public void truncate(IContext context) {
		updatePrimary(context, FastOrmStringTemplates.truncateTempTable);
	}

	@Override
	public int populate(IContext context, int page) {
		IFastOrmContainer fastOrm = context.getFastOrm();
		int size = fastOrm.getBatchSize();
		int start = size * page;
		return updatePrimary(context, FastOrmStringTemplates.populateAllEntitiesTempTable, FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	@Override
	public void drain(IContext context) {
		drainPrimary(context, FastOrmStringTemplates.drainPrimaryTable);
	}

	@Override
	public void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn primary) {
		Maps.addToMapOfLinkedMaps(entityToColumnsAndTypes, primary, primary.getIdColumn(), primary.getIdType());
	}

	@Override
	public void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn primary) {
		String idColumn = primary.getIdColumn();
		Maps.addToMapOfLinkedMaps(entityToColumnsToRowGenerator, primary, idColumn, new SizeIntegerGenerator(idColumn));
	}

	@Override
	public void createStoredProcedure(IContext context) {
		updatePrimary(context, FastOrmStringTemplates.createAllEntitiesStoredProcedure, FastOrmKeys.procName, myProcName(context));
	}

	@Override
	public void drainFromStoredProcedure(IContext readContext, int page) {
		IFastOrmContainer fastOrm = readContext.getFastOrm();
		int size = fastOrm.getBatchSize();
		int start = size * page;
		drainPrimary(readContext, FastOrmStringTemplates.drainFromStoredProcedureWithStartAndSize,//
				FastOrmKeys.procName, myProcName(readContext), FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	private Object myProcName(IContext context) {
		return makeProcName(context.getFastOrm().getEntityDefn(), FastOrmConstants.allEntitiesPostfix);
	}

	@Override
	public void dropStoredProcedure(IContext readContext) {
		updatePrimary(readContext, FastOrmStringTemplates.dropStoredProcedure, "procName", super.makeProcName(readContext.getFastOrm().getEntityDefn(), FastOrmConstants.allEntitiesPostfix));
	}
}
