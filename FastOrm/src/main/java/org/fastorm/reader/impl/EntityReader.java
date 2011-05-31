package org.fastorm.reader.impl;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.impl.FastOrmServices;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.aggregators.IAggregator;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.collections.SimpleLists;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.Functions;
import org.fastorm.utilities.functions.IAggregateFunction;
import org.fastorm.utilities.functions.IFoldFunction;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.ISimpleMap;

public class EntityReader<T> implements IEntityReader<T> {

	private final IFastOrmContainer fastOrm;
	private final IFunction1<ISimpleMap<String, Object>, T> convertor;

	/** When this is called...T is a Map<String,Object> */
	@SuppressWarnings("unchecked")
	public EntityReader(IFastOrmContainer fastOrm) {
		this(fastOrm, Functions.identity);
	}

	public EntityReader(IFastOrmContainer fastOrm, IFunction1<ISimpleMap<String, Object>, T> convertor) {
		this.convertor = convertor;
		this.fastOrm = fastOrm;
	}

	public <To> IEntityReader<To> readerFor(IFunction1<T, To> convertor) {
		IFunction1<ISimpleMap<String, Object>, To> newConvertor = Functions.compose(this.convertor, convertor);
		return new EntityReader<To>(fastOrm, newConvertor);
	}

	public Iterable<T> getIterator() {
		Iterable<IDataSet> dataSets = getDataSets();
		Iterable<T> result = Iterables.split(dataSets, new IFunction1<IDataSet, Iterable<T>>() {
			public Iterable<T> apply(IDataSet from) throws Exception {
				if (from == null)
					throw new NullPointerException();
				List<ISimpleMap<String, Object>> list = from.slowList();
				return Iterables.map(list, convertor);
			}
		});
		return result;
	}

	public Future<Void> processAll(ICallback<T> callback) {
		FastOrmServices optimisation = fastOrm.getServices();
		return Iterables.processCallbacks(optimisation.service, getIterator(), callback);
	}

	public <Result> Future<IAggregator<T, Result>> merge(final IAggregator<T, Result> aggregator) {
		final FastOrmServices optimisation = fastOrm.getServices();
		return optimisation.service.submit(new Callable<IAggregator<T, Result>>() {
			public IAggregator<T, Result> call() throws Exception {
				for (IDataSet dataSet : getDataSets())
					SimpleLists.mapAggregate(fastOrm.getMaxForOneThread(), optimisation.pool, dataSet, aggregator, convertor).get();
				return aggregator;
			}
		});
	}

	public <Result> Future<IAggregator<Result, Result>> twoStageMerge(final IAggregator<Result, Result> middleAggregator, final Callable<IAggregator<T, Result>> leafAggregator) {
		final FastOrmServices optimisation = fastOrm.getServices();
		return optimisation.service.submit(new Callable<IAggregator<Result, Result>>() {
			public IAggregator<Result, Result> call() throws Exception {
				for (IDataSet dataSet : getDataSets())
					SimpleLists.mapTwoAggregators(fastOrm.getMaxForOneThread(), optimisation.pool, dataSet, middleAggregator, leafAggregator, convertor).get();
				return middleAggregator;
			}
		});
	}

	public <Result> Future<Result> reduce(final IAggregateFunction<Result> aggregateFunction, final IFoldFunction<T, Result> foldFunction, Result initial) {
		final FastOrmServices optimisation = fastOrm.getServices();
		return Iterables.fold(optimisation.service, getDataSets(), new IFoldFunction<IDataSet, Result>() {
			public Result apply(IDataSet value, Result initial) {
				try {
					Future<Result> fromDataSet = SimpleLists.mapReduce(fastOrm.getMaxForOneThread(), optimisation.pool, value, aggregateFunction, foldFunction, convertor, initial);
					Result result = aggregateFunction.apply(initial, fromDataSet.get());
					return result;
				} catch (Exception e) {
					throw WrappedException.wrap(e);
				}
			}
		}, initial);
	}

	public void processDataSets(ICallback<IDataSet> dataSetCallback) {
		Iterables.processCallbacks(getDataSets(), dataSetCallback);
	}

	public Iterable<IDataSet> getDataSets() {
		Iterable<IDataSet> dataSets = fastOrm.getEntityReaderThin().dataSets(fastOrm);
		return dataSets;
	}

	public Iterator<T> iterator() {
		return getIterator().iterator();
	}

}
