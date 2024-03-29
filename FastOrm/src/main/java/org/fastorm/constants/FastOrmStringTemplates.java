package org.fastorm.constants;

import java.util.Arrays;
import java.util.Collection;

public class FastOrmStringTemplates {
	public static final String dropTempTable = "dropTempTable";
	public static final String createAllEntitiesTempTable = "createAllEntitiesTempTable";
	public static final String populateAllEntitiesTempTable = "populateAllEntitiesTempTable";
	public static final String drainPrimaryTable = "drainPrimaryTable";
	public static final String createOneToManyTempTable = "createOneToManyTempTable";
	public static final String populateOneToManyTempTable = "populateOneToManyTempTable";
	public static final String drainLeafOneToManyTable = "drainLeafOneToManyTable";

	public static final String drainSecondaryTable = "drainSecondaryTable";

	public static final String createManyToOneTempTable = "createManyToOneTempTable";
	public static final String populateManyToOneTempTable = "populateManyToOneTempTable";
	public static final String truncateTempTable = "truncateTempTable";
	public static final String addIndexToTempTable = "addIndexToTempTable";

	public static final String dropStoredProcedure = "dropStoredProcedure";
	public static final String createAllEntitiesStoredProcedure = "createAllEntitiesStoredProcedure";
	public static final String createOneToManyStoredProcedure = "createOneToManyStoredProcedure";
	public static final String createOneToManyLeafStoredProcedure = "createOneToManyLeafStoredProcedure";
	public static final String createManyToOneStoredProcedure = "createManyToOneStoredProcedure";
	public static final String drainFromStoredProcedureWithStartAndSize = "drainFromStoredProcedureWithStartAndSize";
	public static final String drainFromStoredProcedure = "drainFromStoredProcedure";
	public static final String createUpdateTempTable = "createUpdateTempTable";
	public static final String update = "update";

	public static Collection<? extends String> internalTemplateNames() {
		return Arrays.asList("makeTemp");
	}

}
