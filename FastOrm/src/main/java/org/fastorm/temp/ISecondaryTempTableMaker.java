package org.fastorm.temp;

import java.util.List;
import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.context.ReadContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;

public interface ISecondaryTempTableMaker {

	boolean accept(Map<String, String> parent, Map<String, String> child);

	void drop(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn child);

	void create(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child);

	void truncate(IFastOrmContainer fastOrm, ReadContext context, IEntityDefn parent, IEntityDefn child);

	int populate(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child);

	Object findDataIn(IGetDrainedTableForEntityDefn getter, IEntityDefn parentDefn, int parentIndex, IEntityDefn childDefn, int childIndex);

	void drain(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child);

	void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn parent, IEntityDefn child);

	void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn parent, IEntityDefn child);

	void enrichListOfColumnsToIndex(Map<IEntityDefn, List<String>> columnsToIndex, IEntityDefn parent, IEntityDefn child);

	void dropStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child);

	void createStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child);

	void drainFromStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext, IEntityDefn parent, IEntityDefn child);

}
