package org.fastorm.reader.impl;

import static org.fastorm.dataSet.DataSetMother.abc;
import static org.fastorm.dataSet.DataSetMother.def;
import static org.fastorm.dataSet.DataSetMother.gh;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

import org.fastorm.api.IJob;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.impl.Job;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.impl.EntityDefn;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.callbacks.MemoryCallback;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.maps.ISimpleMap;

public class AbstractEntityReaderTest extends TestCase {

	protected IJob job;

	public void testDataSets() {
		checkDataSets();
		checkDataSets(abc, def, gh);
	}

	private void checkDataSets(IDataSet... dataSets) {
		EntityReaderThinMock mock = new EntityReaderThinMock(dataSets);
		IFastOrmContainer expectedFastOrm = (IFastOrmContainer) job.withThinInterface(mock).withEntityDefn(new EntityDefn());
		IEntityReader<ISimpleMap<String, Object>> reader = expectedFastOrm.makeReader();
		mock.setExpectedFastOrm(expectedFastOrm);

		checkDataSetsInReader(reader, dataSets);
		checkDataSetsInReader(reader, dataSets);
	}

	private void checkDataSetsInReader(IEntityReader<ISimpleMap<String, Object>> reader, IDataSet... dataSets) {
		MemoryCallback<IDataSet> memoryCallbackForInProcess = new MemoryCallback<IDataSet>();
		reader.processDataSets(memoryCallbackForInProcess);
		assertEquals(Arrays.asList(dataSets), memoryCallbackForInProcess.getResult());
		assertEquals(Arrays.asList(dataSets), Iterables.list(reader.getDataSets()));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setUp() throws Exception {
		IEntityDefn entityDefn = new EntityDefn(null, Collections.EMPTY_MAP, Collections.EMPTY_LIST);
		job = makeFactory(entityDefn);
	}

	protected IJob makeFactory(IEntityDefn entityDefn) {
		return new Job().withEntityDefn(entityDefn);
	}

}
