package org.fastorm.dataGenerator;

import java.util.Map;

import org.fastorm.defns.IEntityDefn;

public class NoExtraDataGenerator implements IExtraDataGenerator {

	@Override
	public void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn entityDefn) {

	}

	@Override
	public void enrichValues(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn primary) {

	}

}
