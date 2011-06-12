package org.fastorm.reader.impl;

import junit.framework.Assert;

import org.fastorm.api.IJob;
import org.fastorm.context.IContext;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.reader.IEntityReaderThin;

public class EntityReaderThinMock implements IEntityReaderThin {

	private final IMutableDataSet[] dataSets;
	private IJob job;

	public EntityReaderThinMock(IMutableDataSet... dataSets) {
		this.dataSets = dataSets;
	}

	public void setExpectedJob(IJob job) {
		this.job = job;
	}

	@Override
	public IMutableDataSet readPage(int page, IContext context) {
		Assert.assertSame(this.job, context.getFastOrm());
		if (page >= dataSets.length)
			return null;
		return dataSets[page];
	}

}
