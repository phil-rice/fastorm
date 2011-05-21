package org.fastorm.reader.impl;

import static org.fastorm.dataSet.DataSetMother.abc;
import static org.fastorm.dataSet.DataSetMother.def;
import static org.fastorm.dataSet.DataSetMother.gh;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.MemoryCallback;
import org.fastorm.api.impl.FastOrm;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.impl.EntityDefn;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.ISimpleMap;
import org.fastorm.utilities.Iterables;

public class AbstractEntityReaderTest extends TestCase {

	protected IFastOrm fastOrm;

	public void testDataSets() {
		checkDataSets();
		checkDataSets(abc, def, gh);
	}

	private void checkDataSets(IDataSet... dataSets) {
		EntityReaderThinMock mock = new EntityReaderThinMock(dataSets);
		IFastOrmContainer expectedFastOrm = (IFastOrmContainer) fastOrm.withThinInterface(mock).withEntityDefn(new EntityDefn());
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
		fastOrm = makeFactory(entityDefn);
	}

	protected IFastOrm makeFactory(IEntityDefn entityDefn) {
		return new FastOrm().withEntityDefn(entityDefn);
	}

}
