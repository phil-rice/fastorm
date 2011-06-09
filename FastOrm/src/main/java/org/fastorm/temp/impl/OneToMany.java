package org.fastorm.temp.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.constants.FastOrmKeys;
import org.fastorm.constants.FastOrmMessages;
import org.fastorm.constants.FastOrmStringTemplates;
import org.fastorm.context.ReadContext;
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

	@Override
	public boolean accept(Map<String, String> parent, Map<String, String> child) {
		return child.containsKey(FastOrmKeys.childLink);
	}

	@Override
	public void create(IFastOrmContainer fastOrm, ReadContext context, IEntityDefn parent, IEntityDefn child) {
		update(fastOrm, context, FastOrmStringTemplates.createOneToManyTempTable, child, FastOrmKeys.parentIdColumn, parent.getIdColumn(), FastOrmKeys.parentIdType, parent.getIdType());
		if (fastOrm.indexSecondaryTables())
			update(fastOrm, context, FastOrmStringTemplates.addIndexToTempTable, child);
	}

	@Override
	public void drop(IFastOrmContainer fastOrm, ReadContext context, IEntityDefn child) {
		update(fastOrm, context, FastOrmStringTemplates.dropTempTable, child);
	}

	@Override
	public void truncate(IFastOrmContainer fastOrm, ReadContext context, IEntityDefn parent, IEntityDefn child) {
		update(fastOrm, context, FastOrmStringTemplates.truncateTempTable, child);
	}

	@Override
	public int populate(IFastOrmContainer fastOrm, ReadContext context, IEntityDefn parent, IEntityDefn child) {
		if (!fastOrm.optimiseLeafAccess() || child.getChildren().size() > 0)
			return update(fastOrm, context, FastOrmStringTemplates.populateOneToManyTempTable, child,//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
		else
			return 0;
	}

	@Override
	public void drain(IFastOrmContainer fastOrm, ReadContext context, IEntityDefn parent, IEntityDefn child) {
		if (!fastOrm.optimiseLeafAccess() || child.getChildren().size() > 0)
			drainSecondary(fastOrm, context, child, FastOrmStringTemplates.drainSecondaryTable);
		else
			drainSecondary(fastOrm, context, child, FastOrmStringTemplates.drainLeafOneToManyTable,//
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
	public void createStoredProcedure(IFastOrmContainer fastOrm, ReadContext context, IEntityDefn parent, IEntityDefn child) {
		if (!fastOrm.optimiseLeafAccess() || child.getChildren().size() > 0)
			update(fastOrm, context, FastOrmStringTemplates.createOneToManyStoredProcedure, child, "procName", makeProcName(child),//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
		else
			update(fastOrm, context, FastOrmStringTemplates.createOneToManyLeafStoredProcedure, child, "procName", makeProcName(child),//
					FastOrmKeys.parentTemp, parent.getTempTableName(),//
					FastOrmKeys.parentIdColumn, parent.getIdColumn());
	}

	@Override
	public void drainFromStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child) {
		drainSecondary(fastOrm, readContext, child, FastOrmStringTemplates.drainFromStoredProcedure, FastOrmKeys.procName, makeProcName(child));
	}

	@Override
	public void dropStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child) {
		update(fastOrm, readContext, FastOrmStringTemplates.dropStoredProcedure, child, FastOrmKeys.procName, makeProcName(child));
	}
}
