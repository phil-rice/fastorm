package org.fastorm.defns;

import java.util.Map;

import org.fastorm.constants.FastOrmKeys;
import org.fastorm.defns.impl.MapToEntityDefn;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.maps.Maps;

public class EntityDefnTestFixture {
	public static Map<String, Object> personAddressTelephoneEmployerMap = Maps.makeMap(//
			FastOrmKeys.entityName, "person",//
			FastOrmKeys.tableName, "person",//
			FastOrmKeys.tempTableName, "person_temp",//
			FastOrmKeys.idColumn, "id",//
			FastOrmKeys.idType, "integer",//
			FastOrmKeys.versionColumn, "p_version",//
			FastOrmKeys.useTemporaryTable, true,//
			FastOrmKeys.maxLinesPerBatch, "100",//
			"address", Maps.makeMap(//
					FastOrmKeys.entityName, "address",//
					FastOrmKeys.tableName, "address",//
					FastOrmKeys.tempTableName, "address_temp",//
					FastOrmKeys.idColumn, "a_id",//
					FastOrmKeys.idType, "integer",//
					FastOrmKeys.versionColumn, "version", //
					FastOrmKeys.useTemporaryTable, true,//
					FastOrmKeys.maxLinesPerBatch, "100",//
					FastOrmKeys.childLink, "a_person"),//
			"telephone", Maps.makeMap(//
					FastOrmKeys.entityName, "telephone",//
					FastOrmKeys.tableName, "telephone",//
					FastOrmKeys.tempTableName, "telephone_temp",//
					FastOrmKeys.idColumn, "id",//
					FastOrmKeys.idType, "integer",//
					FastOrmKeys.versionColumn, "version", //
					FastOrmKeys.useTemporaryTable, true,//
					FastOrmKeys.maxLinesPerBatch, "100",//
					FastOrmKeys.childLink, "t_person"),//
			"employer", Maps.makeMap(//
					FastOrmKeys.entityName, "employer",//
					FastOrmKeys.tableName, "employer",//
					FastOrmKeys.tempTableName, "employer_temp",//
					FastOrmKeys.idColumn, "id",//
					FastOrmKeys.idType, "integer",//
					FastOrmKeys.versionColumn, "version", //
					FastOrmKeys.useTemporaryTable, true,//
					FastOrmKeys.maxLinesPerBatch, "100",//
					FastOrmKeys.parentLink, "p_employer"));//

	public static IEntityDefn person = new MapToEntityDefn().create(new TempTableMakerFactory(), null, personAddressTelephoneEmployerMap);
	public static IEntityDefn address = person.getChildren().get(0);
	public static IEntityDefn telephone = person.getChildren().get(1);
	public static IEntityDefn employer = person.getChildren().get(2);

}
