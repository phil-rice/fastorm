package org.fastorm.api;

public interface IJobOptimisiations {
	boolean indexPrimaryTables();

	boolean indexSecondaryTables();

	boolean useTemporaryTables();

	boolean createAnddropProceduresAtStartOfRun();

	boolean optimiseLeafAccess();

}
