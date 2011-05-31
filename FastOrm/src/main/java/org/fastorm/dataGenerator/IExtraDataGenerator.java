package org.fastorm.dataGenerator;

import java.util.Map;

import org.fastorm.defns.IEntityDefn;

public interface IExtraDataGenerator {
	void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn entityDefn);

	void enrichValues(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn primary);

}
