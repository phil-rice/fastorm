package org.fastorm.temp.impl;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.IContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.utilities.maps.Maps;

public class AllEntitiesTempTableMaker extends AbstractSqlExecutor implements IPrimaryTempTableMaker {

	@Override
	public void clean(IContext context) {
		updatePrimary(context, FastOrmStringTemplates.truncateTempTable);
		updatePrimary(context, FastOrmStringTemplates.dropTempTable);
	}

	@Override
	public void create(IContext context) {
		updatePrimary(context, FastOrmStringTemplates.createAllEntitiesTempTable);
		if (context.getFastOrm().getOptimisations().indexPrimaryTables())
			updatePrimary(context, FastOrmStringTemplates.addIndexToTempTable);
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

}
