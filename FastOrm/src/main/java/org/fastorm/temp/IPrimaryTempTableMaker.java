package org.fastorm.temp;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.reader.impl.OrmReadContext;

public interface IPrimaryTempTableMaker {

	void drop(IFastOrmContainer fastOrm, OrmReadContext ormReadContext);

	void create(IFastOrmContainer fastOrm, OrmReadContext ormReadContext);

	void truncate(IFastOrmContainer fastOrm, OrmReadContext context);

	int populate(IFastOrmContainer fastOrm, OrmReadContext ormReadContext, int page);

	IDrainedTableData drain(IFastOrmContainer fastOrm, OrmReadContext ormReadContext);

	void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn primary);

	void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn primary);

}
