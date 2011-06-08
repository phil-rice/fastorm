package org.fastorm.reader.impl;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.fastorm.api.IJob;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.reader.IEntityReaderThin;

public class EntityReaderThinMock implements IEntityReaderThin {

	private final Iterable<IDataSet> result;
	private IJob job;

	public EntityReaderThinMock(IDataSet... dataSets) {
		result = Collections.unmodifiableCollection(Arrays.asList(dataSets));
	}

	public void setExpectedFastOrm(IJob job) {
		this.job = job;
	}

	@Override
	public <T> Iterable<IDataSet> dataSets(IFastOrmContainer fastOrm) {
		Assert.assertSame(this.job, fastOrm);
		return result;
	}

}
