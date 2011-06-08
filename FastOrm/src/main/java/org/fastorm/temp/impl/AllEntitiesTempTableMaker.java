package org.fastorm.temp.impl;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmConstants;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.ReadContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.utilities.maps.Maps;

public class AllEntitiesTempTableMaker extends AbstractSqlExecutor implements IPrimaryTempTableMaker {

	@Override
	public void drop(IFastOrmContainer fastOrm, ReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.dropTempTable);
	}

	@Override
	public void create(IFastOrmContainer fastOrm, ReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.createAllEntitiesTempTable);
		if (fastOrm.indexPrimaryTables())
			updatePrimary(fastOrm, context, FastOrmStringTemplates.addIndexToTempTable);
	}

	@Override
	public void truncate(IFastOrmContainer fastOrm, ReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.truncateTempTable);
	}

	@Override
	public int populate(IFastOrmContainer fastOrm, ReadContext context, int page) {
		int size = fastOrm.getBatchSize();
		int start = size * page;
		return updatePrimary(fastOrm, context, FastOrmStringTemplates.populateAllEntitiesTempTable, FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	@Override
	public void drain(final IFastOrmContainer fastOrm, ReadContext context) {
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
	public void createStoredProcedure(IFastOrmContainer fastOrm, ReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.createAllEntitiesStoredProcedure, FastOrmKeys.procName, myProcName(fastOrm));
	}

	@Override
	public void drainFromStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext, int page) {
		int size = fastOrm.getBatchSize();
		int start = size * page;
		drainPrimary(fastOrm, readContext, FastOrmStringTemplates.drainFromStoredProcedureWithStartAndSize,//
				FastOrmKeys.procName, myProcName(fastOrm), FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	private Object myProcName(IFastOrmContainer fastOrm) {
		return makeProcName(fastOrm.getEntityDefn(), FastOrmConstants.allEntitiesPostfix);
	}

	@Override
	public void dropStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext) {
		updatePrimary(fastOrm, readContext, FastOrmStringTemplates.dropStoredProcedure, "procName", super.makeProcName(fastOrm.getEntityDefn(), FastOrmConstants.allEntitiesPostfix));
	}
}
