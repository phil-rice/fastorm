package org.fastorm.reader.impl;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.reader.IEntityReaderThin;

public class EntityReaderThinMock implements IEntityReaderThin {

	private final Iterable<IDataSet> result;
	private IFastOrm fastOrm;

	public EntityReaderThinMock(IDataSet... dataSets) {
		result = Collections.unmodifiableCollection(Arrays.asList(dataSets));
	}

	public void setExpectedFastOrm(IFastOrm fastOrm) {
		this.fastOrm = fastOrm;
	}

	@Override
	public <T> Iterable<IDataSet> dataSets(IFastOrmContainer fastOrm) {
		Assert.assertSame(this.fastOrm, fastOrm);
		return result;
	}

}
