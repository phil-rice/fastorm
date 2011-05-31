package org.fastorm.utilities.aggregators;

import java.util.List;

import org.fastorm.utilities.collections.Iterables;

public class ListsToIterablAggregator<T> implements IAggregator<List<T>, Iterable<T>> {

	ListAggregator<List<T>> listAggregator;

	public ListsToIterablAggregator(boolean threadSafe) {
		listAggregator = new ListAggregator<List<T>>(threadSafe);
	}

	public void add(List<T> t) {
		listAggregator.add(t);
	}

	public Iterable<T> result() {
		return Iterables.split(listAggregator.result());
	}

}
