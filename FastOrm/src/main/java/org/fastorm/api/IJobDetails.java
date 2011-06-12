package org.fastorm.api;

import org.fastorm.api.impl.JobDetails;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.impl.AllEntitiesTempTableMaker;

public interface IJobDetails {
	IEntityDefn getEntityDefn();

	IPrimaryTempTableMaker getPrimaryTempTableMaker();

	int getBatchSize();

	int getMaxForOneThread();

	boolean createAnddropProceduresAtStartOfRun();

	IJobOptimisations getOptimisations();

	static class Utils {

		public static IJobDetails allEntities(IEntityDefn entityDefn, int batchSize) {
			return new JobDetails(IJobOptimisations.Utils.usualBest(), entityDefn, new AllEntitiesTempTableMaker(), batchSize, batchSize, true);
		}

	}

}
