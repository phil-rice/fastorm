package org.fastorm.temp;

import java.util.List;
import java.util.Map;

import org.fastorm.context.IContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;

public interface ISecondaryTempTableMaker {

	boolean accept(Map<String, String> parent, Map<String, String> child);

	void drop(IContext readContext, IEntityDefn child);

	void create(IContext context, IEntityDefn parent, IEntityDefn child);

	void truncate(IContext context, IEntityDefn parent, IEntityDefn child);

	int populate(IContext readContext, IEntityDefn parent, IEntityDefn child);

	Object findDataIn(IGetDrainedTableForEntityDefn getter, IEntityDefn parentDefn, int parentIndex, IEntityDefn childDefn, int childIndex);

	void drain(IContext readContext, IEntityDefn parent, IEntityDefn child);

	void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn parent, IEntityDefn child);

	void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn parent, IEntityDefn child);

	void enrichListOfColumnsToIndex(Map<IEntityDefn, List<String>> columnsToIndex, IEntityDefn parent, IEntityDefn child);

	void dropStoredProcedure(IContext readContext, IEntityDefn parent, IEntityDefn child);

	void createStoredProcedure(IContext readContext, IEntityDefn parent, IEntityDefn child);

	void drainFromStoredProcedure(IContext readContext, IEntityDefn parent, IEntityDefn child);

}
