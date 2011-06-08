package org.fastorm.api;

import org.fastorm.api.impl.JobDetails;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;

public interface IJobDetails extends IJobOptimisiations {
	IEntityDefn getEntityDefn();

	IPrimaryTempTableMaker getPrimaryTempTableMaker();

	int getMaxForOneThread();

	int getBatchSize();

	int byteBufferSize();

	static class Utils {
		public static IJobDetails withTempTablesForTests(boolean useTempTables) {
			return new JobDetails(null, null, true, true, useTempTables, true, true, 100, 100, 100);
		}
	}
}
