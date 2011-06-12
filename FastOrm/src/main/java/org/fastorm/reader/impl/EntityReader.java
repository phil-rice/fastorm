package org.fastorm.reader.impl;

import java.sql.Connection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.impl.JobServices;
import org.fastorm.context.Context;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.aggregators.IAggregator;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.collections.AbstractFindNextIterable;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.collections.SimpleLists;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.Functions;
import org.fastorm.utilities.functions.IAggregateFunction;
import org.fastorm.utilities.functions.IFoldFunction;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;

public class EntityReader<T> implements IEntityReader<T> {

	private final IFastOrmContainer fastOrm;
	private final IFunction1<ISimpleMapWithIndex<String, Object>, T> convertor;

	/** When this is called...T is a Map<String,Object> */
	@SuppressWarnings("unchecked")
	public EntityReader(IFastOrmContainer fastOrm) {
		this(fastOrm, Functions.identity);
	}

	public EntityReader(IFastOrmContainer fastOrm, IFunction1<ISimpleMapWithIndex<String, Object>, T> convertor) {
		this.convertor = convertor;
		this.fastOrm = fastOrm;
	}

	@Override
	public <To> IEntityReader<To> readerFor(IFunction1<T, To> convertor) {
		IFunction1<ISimpleMapWithIndex<String, Object>, To> newConvertor = Functions.compose(this.convertor, convertor);
		return new EntityReader<To>(fastOrm, newConvertor);
	}

	@Override
	public Iterable<T> getIterator() {
		Iterable<IDataSet> dataSets = getDataSets();
		Iterable<T> result = Iterables.split(dataSets, new IFunction1<IDataSet, Iterable<T>>() {
			@Override
			public Iterable<T> apply(IDataSet from) throws Exception {
				if (from == null)
					throw new NullPointerException();
				return Iterables.map(from, convertor);
			}
		});
		return result;
	}

	@Override
	public Future<Void> processAll(ICallback<T> callback) {
		JobServices optimisation = fastOrm.getServices();
		return Iterables.processCallbacks(optimisation.service, getIterator(), callback);
	}

	@Override
	public <Result> Future<IAggregator<T, Result>> merge(final IAggregator<T, Result> aggregator) {
		final JobServices optimisation = fastOrm.getServices();
		return optimisation.service.submit(new Callable<IAggregator<T, Result>>() {
			@Override
			public IAggregator<T, Result> call() throws Exception {
				for (IDataSet dataSet : getDataSets())
					SimpleLists.mapAggregate(fastOrm.getMaxForOneThread(), optimisation.pool, dataSet, aggregator, convertor).get();
				return aggregator;
			}
		});
	}

	@Override
	public <Result> Future<IAggregator<Result, Result>> twoStageMerge(final IAggregator<Result, Result> middleAggregator, final Callable<IAggregator<T, Result>> leafAggregator) {
		final JobServices optimisation = fastOrm.getServices();
		return optimisation.service.submit(new Callable<IAggregator<Result, Result>>() {
			@Override
			public IAggregator<Result, Result> call() throws Exception {
				for (IDataSet dataSet : getDataSets())
					SimpleLists.mapTwoAggregators(fastOrm.getMaxForOneThread(), optimisation.pool, dataSet, middleAggregator, leafAggregator, convertor).get();
				return middleAggregator;
			}
		});
	}

	@Override
	public <Result> Future<Result> reduce(final IAggregateFunction<Result> aggregateFunction, final IFoldFunction<T, Result> foldFunction, Result initial) {
		final JobServices optimisation = fastOrm.getServices();
		return Iterables.fold(optimisation.service, getDataSets(), new IFoldFunction<IDataSet, Result>() {
			@Override
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

	@Override
	public void processDataSets(ICallback<IDataSet> dataSetCallback) {
		Iterables.processCallbacks(getDataSets(), dataSetCallback);
	}

	@Override
	public Iterable<IDataSet> getDataSets() {
		assert fastOrm.getDataSource() != null;
		return new AbstractFindNextIterable<IDataSet, AtomicInteger>() {
			@Override
			protected IDataSet findNext(AtomicInteger page) throws Exception {
				Connection connection = fastOrm.getDataSource().getConnection();
				Context context = new Context(fastOrm, connection);
				try {
					return fastOrm.getEntityReaderThin().readPage(page.getAndIncrement(), context);
				} finally {
					connection.close();
				}
			}

			@Override
			protected AtomicInteger reset() throws Exception {
				return new AtomicInteger();
			}
		};
	}

	@Override
	public Iterator<T> iterator() {
		return getIterator().iterator();
	}

}
