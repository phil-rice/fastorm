package org.fastorm.temp.impl;

import java.util.List;
import java.util.Map;

import org.fastorm.api.IJobOptimisations;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.IContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.ParentForeignKeyGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.Maps;

public class ManyToOne extends AbstractSqlExecutor implements ISecondaryTempTableMaker {

	private final IJobOptimisations optimisations;

	public ManyToOne(IJobOptimisations optimisations) {
		this.optimisations = optimisations;
	}

	@Override
	public boolean accept(Map<String, String> parent, Map<String, String> child) {
		return child.containsKey(FastOrmKeys.parentLink);
	}

	@Override
	public void create(IContext context, IEntityDefn parent, IEntityDefn child) {
		update(context, FastOrmStringTemplates.createManyToOneTempTable, child);
	}

	@Override
	public void drop(IContext context, IEntityDefn child) {
		update(context, FastOrmStringTemplates.dropTempTable, child);
	}

	@Override
	public void truncate(IContext context, IEntityDefn parent, IEntityDefn child) {
		update(context, FastOrmStringTemplates.truncateTempTable, child);
	}

	@Override
	public int populate(IContext context, IEntityDefn parent, IEntityDefn child) {
		return update(context, FastOrmStringTemplates.populateManyToOneTempTable, child);
	}

	@Override
	public void drain(IContext context, IEntityDefn parent, IEntityDefn child) {
		drainSecondary(context, child, FastOrmStringTemplates.drainSecondaryTable);
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
	public Object findDataIn(IGetDrainedTableForEntityDefn getter, IEntityDefn parentDefn, int parentIndex, IEntityDefn childDefn, int childIndex) {
		IDrainedTableData parentData = getter.get(parentDefn);
		IDrainedTableData childData = getter.get(childDefn);
		String parentLink = childDefn.parameters().get(FastOrmKeys.parentLink);
		Object childId = parentData.get(parentIndex).get(parentLink);
		List<ISimpleMap<String, Object>> result = childData.findWith(childData.getIdColumnIndex(), childId);
		return result;
	}

	@Override
	public void enrichListOfColumnsToIndex(Map<IEntityDefn, List<String>> columnsToIndex, IEntityDefn parent, IEntityDefn child) {
		Maps.addToList(columnsToIndex, child, child.getIdColumn());
		Maps.addToList(columnsToIndex, parent, child.parameters().get(FastOrmKeys.parentLink));
	}

	@Override
	public void createStoredProcedure(IContext context, IEntityDefn parent, IEntityDefn child) {
		update(context, FastOrmStringTemplates.createManyToOneStoredProcedure, child, "procname", makeProcName(child));
	}

	@Override
	public void drainFromStoredProcedure(IContext readContext, IEntityDefn parent, IEntityDefn child) {
		drainSecondary(readContext, child, FastOrmStringTemplates.drainFromStoredProcedure, FastOrmKeys.procName, makeProcName(child));
	}

	@Override
	public void dropStoredProcedure(IContext readContext, IEntityDefn parent, IEntityDefn child) {
		update(readContext, FastOrmStringTemplates.dropStoredProcedure, child, FastOrmKeys.procName, makeProcName(child));
	}
}
