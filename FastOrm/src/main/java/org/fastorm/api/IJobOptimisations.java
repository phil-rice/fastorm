package org.fastorm.api;

public interface IJobOptimisations {
	boolean indexPrimaryTables();

	boolean indexSecondaryTables();

	boolean useTemporaryTables();

	boolean createAnddropProceduresAtStartOfRun();

	boolean optimiseLeafAccess();

}
