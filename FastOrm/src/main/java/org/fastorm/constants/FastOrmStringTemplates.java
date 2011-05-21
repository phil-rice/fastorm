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
	public static final String drainSecondaryTable = "drainSecondaryTable";
	public static final String createManyToOneTempTable = "createManyToOneTempTable";
	public static final String populateManyToOneTempTable = "populateManyToOneTempTable";
	public static final String truncateTempTable = "truncateTempTable";
	public static final String addIndexToTempTable = "addIndexToTempTable";

	public static Collection<? extends String> internalTemplateNames() {
		return Arrays.asList("makeTemp");
	}

}
