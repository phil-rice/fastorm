package org.fastorm.temp.impl;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.utilities.Maps;

public class AllEntitiesTempTableMaker extends AbstractSqlExecutor implements IPrimaryTempTableMaker {

	@Override
	public void drop(IFastOrmContainer fastOrm, OrmReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.dropTempTable);
	}

	@Override
	public void create(IFastOrmContainer fastOrm, OrmReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.createAllEntitiesTempTable);
		if (fastOrm.getOptions().addIndiciesToPrimaryTemporaryTables)
			updatePrimary(fastOrm, context, FastOrmStringTemplates.addIndexToTempTable);
	}

	@Override
	public void truncate(IFastOrmContainer fastOrm, OrmReadContext context) {
		updatePrimary(fastOrm, context, FastOrmStringTemplates.truncateTempTable);
	}

	@Override
	public int populate(IFastOrmContainer fastOrm, OrmReadContext context, int page) {
		int size = fastOrm.getDataSetSize();
		int start = size * page;
		return updatePrimary(fastOrm, context, FastOrmStringTemplates.populateAllEntitiesTempTable, FastOrmKeys.start, start, FastOrmKeys.size, size);
	}

	@Override
	public IDrainedTableData drain(final IFastOrmContainer fastOrm, OrmReadContext context) {
		return drainPrimary(fastOrm, context, FastOrmStringTemplates.drainPrimaryTable);
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
