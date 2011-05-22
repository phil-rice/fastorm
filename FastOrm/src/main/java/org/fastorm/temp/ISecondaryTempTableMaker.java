package org.fastorm.temp;

import java.util.List;
import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.reader.impl.OrmReadContext;
import org.fastorm.utilities.ISimpleMap;

public interface ISecondaryTempTableMaker {

	boolean accept(Map<String, String> parent, Map<String, String> child);

	void drop(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, IEntityDefn child);

	void create(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, IEntityDefn parent, IEntityDefn child);

	void createStoredProcedure(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, IEntityDefn parent, IEntityDefn child);

	void truncate(IFastOrmContainer fastOrm, OrmReadContext context, IEntityDefn parent, IEntityDefn child);

	int populate(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, IEntityDefn parent, IEntityDefn child);

	List<ISimpleMap<String, Object>> findDataIn(IGetDrainedTableForEntityDefn getter, IDrainedTableData parentData, IDrainedTableData childData, IEntityDefn childDefn, int primaryIndex);

	IDrainedTableData drain(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, IEntityDefn parent, IEntityDefn child);

	void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn parent, IEntityDefn child);

	void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn parent, IEntityDefn child);

	void enrichListOfColumnsToIndex(Map<IEntityDefn, List<String>> columnsToIndex, IEntityDefn parent, IEntityDefn child);

	IDrainedTableData drainFromStoredProcedure(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, IEntityDefn parent, IEntityDefn child);

}
