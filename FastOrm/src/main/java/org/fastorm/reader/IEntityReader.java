package org.fastorm.reader;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.fastorm.api.ICallback;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.utilities.IAggregateFunction;
import org.fastorm.utilities.IFoldFunction;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.aggregators.IAggregator;

public interface IEntityReader<T> extends Iterable<T> {

	<To> IEntityReader<To> readerFor(IFunction1<T, To> convertor);

	Iterable<IDataSet> getDataSets();

	Future<Void> processAll(ICallback<T> callback);

	Iterable<T> getIterator();

	<Result> Future<Result> reduce(final IAggregateFunction<Result> aggregateFunction, final IFoldFunction<T, Result> foldFunction, Result initial);

	<Result> Future<IAggregator<T, Result>> merge(IAggregator<T, Result> folder);

	<Result> Future<IAggregator<Result, Result>> twoStageMerge(IAggregator<Result, Result> middleFolder, Callable<IAggregator<T, Result>> leafFolder);

	void processDataSets(ICallback<IDataSet> dataSetCallback);

}
