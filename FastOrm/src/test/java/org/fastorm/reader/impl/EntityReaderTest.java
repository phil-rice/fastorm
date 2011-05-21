package org.fastorm.reader.impl;

import static org.fastorm.dataSet.DataSetMother.abc;
import static org.fastorm.dataSet.DataSetMother.def;
import static org.fastorm.dataSet.DataSetMother.gh;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.MemoryCallback;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.defns.impl.EntityDefn;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.ISimpleMap;
import org.fastorm.utilities.Iterables;
import org.fastorm.utilities.Maps;
import org.fastorm.utilities.SimpleMaps;
import org.fastorm.utilities.aggregators.IAggregator;
import org.fastorm.utilities.aggregators.SimpleMapAggregator;
import org.fastorm.utilities.foldFunctions.SimpleMapFoldFunction;

public class EntityReaderTest extends AbstractEntityReaderTest {

	public void testMaps() throws InterruptedException, ExecutionException {
		checkMaps();
		checkMaps(abc, def, gh);
	}

	public void testMergers() throws Exception {
		checkMerger();
		checkMerger(abc, def, gh);
	}

	private void checkMerger(IDataSet... dataSets) throws Exception {
		Map<String, Object> expected = SimpleMaps.merge(Iterables.split(Arrays.asList(dataSets), new IFunction1<IDataSet, Iterable<ISimpleMap<String, Object>>>() {
			public List<ISimpleMap<String, Object>> apply(IDataSet from) throws Exception {
				return from.getList();
			}
		}));

		EntityReaderThinMock mock = new EntityReaderThinMock(dataSets);
		IFastOrm expectedFastOrm = fastOrm.withThinInterface(mock).withEntityDefn(new EntityDefn());
		IEntityReader<ISimpleMap<String, Object>> reader = expectedFastOrm.makeReader();
		mock.setExpectedFastOrm(expectedFastOrm);

		SimpleMapFoldFunction<String, Object> aggregatorAndFolder = new SimpleMapFoldFunction<String, Object>();
		ISimpleMap<String, Object> reduceResult = reader.reduce(aggregatorAndFolder, aggregatorAndFolder, SimpleMaps.<String, Object> empty()).get();
		assertEquals(expected, Maps.fromSimpleMap(reduceResult));

		SimpleMapAggregator<String, Object> mergerforSingleMerge = new SimpleMapAggregator<String, Object>();
		ISimpleMap<String, Object> result = reader.merge(mergerforSingleMerge).get().result();
		assertEquals(expected, Maps.fromSimpleMap(result));

		SimpleMapAggregator<String, Object> mainMerger = new SimpleMapAggregator<String, Object>();
		reader.<ISimpleMap<String, Object>> twoStageMerge(mainMerger, new Callable<IAggregator<ISimpleMap<String, Object>, ISimpleMap<String, Object>>>() {
			public IAggregator<ISimpleMap<String, Object>, ISimpleMap<String, Object>> call() throws Exception {
				return new SimpleMapAggregator<String, Object>();
			}
		}).get();
		assertEquals(expected, Maps.fromSimpleMap(mainMerger.result()));
	}

	private void checkMaps(IDataSet... dataSets) throws InterruptedException, ExecutionException {
		Iterable<ISimpleMap<String, Object>> maps = Iterables.split(Arrays.asList(dataSets), new IFunction1<IDataSet, Iterable<ISimpleMap<String, Object>>>() {
			public Iterable<ISimpleMap<String, Object>> apply(IDataSet from) throws Exception {
				return from.getList();
			}
		});
		EntityReaderThinMock mock = new EntityReaderThinMock(dataSets);
		IFastOrm actualFastOrm = fastOrm.withThinInterface(mock).withEntityDefn(new EntityDefn());
		IEntityReader<ISimpleMap<String, Object>> reader = actualFastOrm.makeReader();
		mock.setExpectedFastOrm(actualFastOrm);

		checkMapsInReader(reader, maps);
		checkMapsInReader(reader, maps);

	}

	private void checkMapsInReader(IEntityReader<ISimpleMap<String, Object>> reader, Iterable<ISimpleMap<String, Object>> maps) throws InterruptedException, ExecutionException {
		MemoryCallback<ISimpleMap<String, Object>> memoryCallback = new MemoryCallback<ISimpleMap<String, Object>>();
		reader.processAll(memoryCallback).get();
		List<ISimpleMap<String, Object>> expected = Iterables.list(maps);
		assertEquals(expected, memoryCallback.getResult());

		assertEquals(expected, Iterables.list(reader.getIterator()));

	}
}
