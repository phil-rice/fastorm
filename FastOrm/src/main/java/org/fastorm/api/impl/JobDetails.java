package org.fastorm.api.impl;

import org.fastorm.api.IJobDetails;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;

public class JobDetails implements IJobDetails {
	protected final IEntityDefn entityDefn;
	protected final boolean indexPrimaryTables;
	protected final boolean indexSecondaryTables;
	protected final boolean useTemporaryTables;
	protected final boolean createAnddropProceduresAtStartOfRun;
	protected final boolean optimiseLeafAccess;
	protected final int batchSize;
	protected final int maxForOneThread;
	protected final int byteBufferSize;
	protected final IPrimaryTempTableMaker primaryTempTableMaker;

	public JobDetails(IEntityDefn entityDefn, IPrimaryTempTableMaker primaryTempTableMaker, boolean indexPrimaryTables, boolean indexSecondaryTables, boolean useTemporaryTables, boolean createAnddropProceduresAtStartOfRun, boolean optimiseLeafAccess, int batchSize, int maxForOneThread, int byteBufferSize) {
		this.entityDefn = entityDefn;
		this.primaryTempTableMaker = primaryTempTableMaker;
		this.indexPrimaryTables = indexPrimaryTables;
		this.indexSecondaryTables = indexSecondaryTables;
		this.useTemporaryTables = useTemporaryTables;
		this.createAnddropProceduresAtStartOfRun = createAnddropProceduresAtStartOfRun;
		this.optimiseLeafAccess = optimiseLeafAccess;
		this.batchSize = batchSize;
		this.maxForOneThread = maxForOneThread;
		this.byteBufferSize = byteBufferSize;
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
	public int getBatchSize() {
		return batchSize;
	}

	@Override
	public boolean indexPrimaryTables() {
		return indexPrimaryTables;
	}

	@Override
	public boolean indexSecondaryTables() {
		return indexSecondaryTables;
	}

	@Override
	public boolean useTemporaryTables() {
		return useTemporaryTables;
	}

	@Override
	public boolean createAnddropProceduresAtStartOfRun() {
		return createAnddropProceduresAtStartOfRun;
	}

	@Override
	public boolean optimiseLeafAccess() {
		return optimiseLeafAccess;
	}

	@Override
	public int byteBufferSize() {
		return byteBufferSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + batchSize;
		result = prime * result + byteBufferSize;
		result = prime * result + (createAnddropProceduresAtStartOfRun ? 1231 : 1237);
		result = prime * result + (indexPrimaryTables ? 1231 : 1237);
		result = prime * result + (indexSecondaryTables ? 1231 : 1237);
		result = prime * result + maxForOneThread;
		result = prime * result + (optimiseLeafAccess ? 1231 : 1237);
		result = prime * result + (useTemporaryTables ? 1231 : 1237);
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
		if (byteBufferSize != other.byteBufferSize)
			return false;
		if (createAnddropProceduresAtStartOfRun != other.createAnddropProceduresAtStartOfRun)
			return false;
		if (indexPrimaryTables != other.indexPrimaryTables)
			return false;
		if (indexSecondaryTables != other.indexSecondaryTables)
			return false;
		if (maxForOneThread != other.maxForOneThread)
			return false;
		if (optimiseLeafAccess != other.optimiseLeafAccess)
			return false;
		if (useTemporaryTables != other.useTemporaryTables)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JobDetails [indexPrimaryTables=" + indexPrimaryTables + ", indexSecondaryTables=" + indexSecondaryTables + ", useTemporaryTables=" + useTemporaryTables + ", createAnddropProceduresAtStartOfRun=" + createAnddropProceduresAtStartOfRun + ", optimiseLeafAccess=" + optimiseLeafAccess + ", batchSize=" + batchSize + ", maxForOneThread=" + maxForOneThread + ", byteBufferSize=" + byteBufferSize + "]";
	}

}
