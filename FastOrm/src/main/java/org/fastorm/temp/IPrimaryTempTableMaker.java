package org.fastorm.temp;

import java.util.Map;

import org.fastorm.context.IContext;
import org.fastorm.dataGenerator.IGenerator;
import org.fastorm.defns.IEntityDefn;

public interface IPrimaryTempTableMaker {

	void drop(IContext readContext);

	void create(IContext readContext);

	void truncate(IContext context);

	int populate(IContext readContext, int page);

	void drain(IContext readContext);

	void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn primary);

	void enrichWithGenerators(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn primary);

	void createStoredProcedure(IContext readContext);

	void drainFromStoredProcedure(IContext readContext, int page);

	void dropStoredProcedure(IContext readContext);
}
