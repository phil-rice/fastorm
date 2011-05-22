package org.fastorm.temp.impl;

import java.util.List;
import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.ParentForeignKeyGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.ISimpleMap;
import org.fastorm.utilities.Maps;

public class ManyToOne extends AbstractSqlExecutor implements ISecondaryTempTableMaker {

	@Override
	public boolean accept(Map<String, String> parent, Map<String, String> child) {
		return child.containsKey(FastOrmKeys.parentLink);
	}

	@Override
	public void create(IFastOrmContainer fastOrm, OrmReadContext context, IEntityDefn parent, IEntityDefn child) {
		update(fastOrm, context, FastOrmStringTemplates.createManyToOneTempTable, child);
	}

	@Override
	public void drop(IFastOrmContainer fastOrm, OrmReadContext context, IEntityDefn child) {
		update(fastOrm, context, FastOrmStringTemplates.dropTempTable, child);
	}

	@Override
	public void truncate(IFastOrmContainer fastOrm, OrmReadContext context, IEntityDefn parent, IEntityDefn child) {
		update(fastOrm, context, FastOrmStringTemplates.truncateTempTable, child);
	}

	@Override
	public int populate(IFastOrmContainer fastOrm, OrmReadContext context, IEntityDefn parent, IEntityDefn child) {
		return update(fastOrm, context, FastOrmStringTemplates.populateManyToOneTempTable, child);
	}

	@Override
	public IDrainedTableData drain(IFastOrmContainer fastOrm, OrmReadContext context, IEntityDefn parent, IEntityDefn child) {
		return drainSecondary(fastOrm, context, child, FastOrmStringTemplates.drainSecondaryTable);
	}

	@Override
	public void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn parent, IEntityDefn child) {
		Map<String, String> childParameters = child.parameters();
		Maps.addToMapOfLinkedMaps(entityToColumnsAndTypes, child, child.getIdColumn(), child.getIdType());
		Maps.addToMapOfLinkedMaps(entityToColumnsAndTypes, parent, childParameters.get(FastOrmKeys.parentLink), child.getIdType());
	}

	@Override
	public void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn parent, IEntityDefn child) {
		String childIdColumn = child.getIdColumn();
		String parentLink = child.parameters().get(FastOrmKeys.parentLink);
		Maps.addToMapOfLinkedMaps(entityToColumnsToRowGenerator, child, childIdColumn, new SizeIntegerGenerator(childIdColumn));
		Maps.addToMapOfLinkedMaps(entityToColumnsToRowGenerator, child, parentLink, new ParentForeignKeyGenerator(parentLink));
	}

	@Override
	public List<ISimpleMap<String, Object>> findDataIn(IGetDrainedTableForEntityDefn getter, IDrainedTableData parentData, IDrainedTableData childData, IEntityDefn childDefn, int primaryIndex) {
		String parentLink = childDefn.parameters().get(FastOrmKeys.parentLink);
		int columnIndex = parentData.getColumnNames().indexOf(parentLink);
		Object childId = parentData.getLine(primaryIndex)[columnIndex];
		List<ISimpleMap<String, Object>> result = childData.findWith(getter, childData.getIdColumnIndex(), childId);
		return result;
	}

	@Override
	public void enrichListOfColumnsToIndex(Map<IEntityDefn, List<String>> columnsToIndex, IEntityDefn parent, IEntityDefn child) {
		Maps.addToList(columnsToIndex, child, child.getIdColumn());
		Maps.addToList(columnsToIndex, parent, child.parameters().get(FastOrmKeys.parentLink));
	}

	@Override
	public void createStoredProcedure(IFastOrmContainer fastOrm, OrmReadContext context, IEntityDefn parent, IEntityDefn child) {
		update(fastOrm, context, FastOrmStringTemplates.createManyToOneStoredProcedure, child, "procname", makeProcName(child));
	}

	@Override
	public IDrainedTableData drainFromStoredProcedure(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, IEntityDefn parent, IEntityDefn child) {
		return drainSecondary(fastOrm, ormReadContext, child, FastOrmStringTemplates.drainFromStoredProcedure, FastOrmKeys.procName, makeProcName(child));
	}

}
