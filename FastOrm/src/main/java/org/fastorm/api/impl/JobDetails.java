package org.fastorm.api.impl;

import org.fastorm.api.IJobDetails;
import org.fastorm.api.IJobOptimisations;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;

public class JobDetails implements IJobDetails {
	protected final IEntityDefn entityDefn;
	protected final int batchSize;
	protected final int maxForOneThread;
	protected final IPrimaryTempTableMaker primaryTempTableMaker;
	private final boolean createAndDropProceduresAtStartOfJob;
	protected final IJobOptimisations optimisations;

	public JobDetails(IJobOptimisations optimisations, IEntityDefn entityDefn, IPrimaryTempTableMaker primaryTempTableMaker, int batchSize, int maxForOneThread, boolean createAndDropProceduresAtStartOfJob) {
		this.optimisations = optimisations;
		this.entityDefn = entityDefn;
		this.primaryTempTableMaker = primaryTempTableMaker;
		this.batchSize = batchSize;
		this.maxForOneThread = maxForOneThread;
		this.createAndDropProceduresAtStartOfJob = createAndDropProceduresAtStartOfJob;
	}

	public IEntityDefn getEntityDefn() {
		return entityDefn;
	};

	@Override
	public IPrimaryTempTableMaker getPrimaryTempTableMaker() {
		return primaryTempTableMaker;
	}

	@Override
	public int getMaxForOneThread() {
		return maxForOneThread;
	}

	@Override
	public IJobOptimisations getOptimisations() {
		return optimisations;
	}

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	@Override
	public boolean createAnddropProceduresAtStartOfRun() {
		return createAndDropProceduresAtStartOfJob;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + batchSize;
		result = prime * result + ((entityDefn == null) ? 0 : entityDefn.hashCode());
		result = prime * result + maxForOneThread;
		result = prime * result + ((primaryTempTableMaker == null) ? 0 : primaryTempTableMaker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobDetails other = (JobDetails) obj;
		if (batchSize != other.batchSize)
			return false;
		if (entityDefn == null) {
			if (other.entityDefn != null)
				return false;
		} else if (!entityDefn.equals(other.entityDefn))
			return false;
		if (maxForOneThread != other.maxForOneThread)
			return false;
		if (primaryTempTableMaker == null) {
			if (other.primaryTempTableMaker != null)
				return false;
		} else if (!primaryTempTableMaker.equals(other.primaryTempTableMaker))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JobDetails [entityDefn=" + entityDefn + ", batchSize=" + batchSize + ", maxForOneThread=" + maxForOneThread + ", primaryTempTableMaker=" + primaryTempTableMaker + "]";
	}

}
