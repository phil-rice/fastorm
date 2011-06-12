package org.fastorm.temp.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.fastorm.api.IJobOptimisations;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmMessages;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.IContext;
import org.fastorm.dataGenerator.ChildForeignKeyGenerator;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataGenerator.SizeIntegerGenerator;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.Maps;

public class OneToMany extends AbstractSqlExecutor implements ISecondaryTempTableMaker {

	private final IJobOptimisations optimisations;

	public OneToMany(IJobOptimisations optimisations) {
		this.optimisations = optimisations;
	}

	@Override
	public boolean accept(Map<String, String> parent, Map<String, String> child) {
		return child.containsKey(FastOrmKeys.childLink);
	}

	@Override
	public void create(IContext context, IEntityDefn parent, IEntityDefn child) {
		update(context, FastOrmStringTemplates.createOneToManyTempTable, child, FastOrmKeys.parentIdColumn, parent.getIdColumn(), FastOrmKeys.parentIdType, parent.getIdType());
		if (optimisations.indexSecondaryTables())
			update(context, FastOrmStringTemplates.addIndexToTempTable, child);
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
		if (!optimisations.optimiseLeafAccess() || child.getChildren().size() > 0)
			return update(context, FastOrmStringTemplates.populateOneToManyTempTable, child,//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
		else
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

	@Override
	public void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn parent, IEntityDefn child) {
		Map<String, String> childParameters = child.parameters();
		Maps.addToMapOfLinkedMaps(entityToColumnsAndTypes, child, child.getIdColumn(), child.getIdType());
		Maps.addToMapOfLinkedMaps(entityToColumnsAndTypes, child, childParameters.get(FastOrmKeys.childLink), parent.getIdType());
	}

	@Override
	public void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn parent, IEntityDefn child) {
		String childIdColumn = child.getIdColumn();
		String childLink = child.parameters().get(FastOrmKeys.childLink);
		Maps.addToMapOfLinkedMaps(entityToColumnsToRowGenerator, child, childIdColumn, new SizeIntegerGenerator(childIdColumn));
		Maps.addToMapOfLinkedMaps(entityToColumnsToRowGenerator, child, childLink, new ChildForeignKeyGenerator(childLink));
	}

	@Override
	public Object findDataIn(IGetDrainedTableForEntityDefn getter, IEntityDefn parentDefn, int parentIndex, IEntityDefn childDefn, int childIndex) {
		IDrainedTableData parentData = getter.get(parentDefn);
		Object parentIdValue = parentData.get(parentIndex).get(parentDefn.getIdColumn());

		IDrainedTableData childData = getter.get(childDefn);
		if (childData == null)
			throw new IllegalStateException(MessageFormat.format(FastOrmMessages.cannotFindChildDataFor, childDefn));
		String childLink = childDefn.parameters().get(FastOrmKeys.childLink);
		int columnIndex = childData.indexOf(childLink);
		List<ISimpleMap<String, Object>> result = childData.findWith(columnIndex, parentIdValue);
		return result;
	}

	@Override
	public void enrichListOfColumnsToIndex(Map<IEntityDefn, List<String>> columnsToIndex, IEntityDefn parent, IEntityDefn child) {
		Maps.addToList(columnsToIndex, child, child.getIdColumn());
		Maps.addToList(columnsToIndex, child, child.parameters().get(FastOrmKeys.childLink));
	}

	@Override
	public void createStoredProcedure(IContext context, IEntityDefn parent, IEntityDefn child) {
		if (!optimisations.optimiseLeafAccess() || child.getChildren().size() > 0)
			update(context, FastOrmStringTemplates.createOneToManyStoredProcedure, child, "procName", makeProcName(child),//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
		else
			update(context, FastOrmStringTemplates.createOneToManyLeafStoredProcedure, child, "procName", makeProcName(child),//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
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
