package org.fastorm.temp;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.context.ReadContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.defns.IEntityDefn;

public interface IPrimaryTempTableMaker {

	void drop(IFastOrmContainer fastOrm, ReadContext readContext);

	void create(IFastOrmContainer fastOrm, ReadContext readContext);

	void truncate(IFastOrmContainer fastOrm, ReadContext context);

	int populate(IFastOrmContainer fastOrm, ReadContext readContext, int page);

	void drain(IFastOrmContainer fastOrm, ReadContext readContext);

	void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn primary);

	void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn primary);

	void createStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext);

	void drainFromStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext, int page);

	void dropStoredProcedure(IFastOrmContainer fastOrm, ReadContext readContext);
}
