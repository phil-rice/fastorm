package org.fastorm.temp.impl;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmConstants;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.OrmReadContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.utilities.maps.Maps;

public class AllEntitiesTempTableMaker extends AbstractSqlExecutor implements IPrimaryTempTableMaker {

	@Override
	public void drop(IFastOrmContainer fastOrm, OrmReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.dropTempTable);
	}

	@Override
	public void create(IFastOrmContainer fastOrm, OrmReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.createAllEntitiesTempTable);
		if (fastOrm.getOptions().indexPrimaryTables)
			updatePrimary(fastOrm, context, FastOrmStringTemplates.addIndexToTempTable);
	}

	@Override
	public void truncate(IFastOrmContainer fastOrm, OrmReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.truncateTempTable);
	}

	@Override
	public int populate(IFastOrmContainer fastOrm, OrmReadContext context, int page) {
		int size = fastOrm.getBatchSize();
		int start = size * page;
		return updatePrimary(fastOrm, context, FastOrmStringTemplates.populateAllEntitiesTempTable, FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	@Override
	public void drain(final IFastOrmContainer fastOrm, OrmReadContext context) {
		drainPrimary(fastOrm, context, FastOrmStringTemplates.drainPrimaryTable);
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
	public void createStoredProcedure(IFastOrmContainer fastOrm, OrmReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.createAllEntitiesStoredProcedure, FastOrmKeys.procName, myProcName(fastOrm));
	}

	@Override
	public void drainFromStoredProcedure(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, int page) {
		int size = fastOrm.getBatchSize();
		int start = size * page;
		drainPrimary(fastOrm, ormReadContext, FastOrmStringTemplates.drainFromStoredProcedureWithStartAndSize,//
				FastOrmKeys.procName, myProcName(fastOrm), FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	private Object myProcName(IFastOrmContainer fastOrm) {
		return makeProcName(fastOrm.getEntityDefn(), FastOrmConstants.allEntitiesPostfix);
	}

	@Override
	public void dropStoredProcedure(IFastOrmContainer fastOrm, OrmReadContext ormReadContext) {
		updatePrimary(fastOrm, ormReadContext, FastOrmStringTemplates.dropStoredProcedure, "procName", super.makeProcName(fastOrm.getEntityDefn(), FastOrmConstants.allEntitiesPostfix));
	}
}
