package org.fastorm.utilities.aggregators;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetOfSetsAggregator<T> implements IAggregator<Set<T>, Set<T>> {

	private Set<Set<T>> result;

	public SetOfSetsAggregator(boolean threadSafe) {
		if (threadSafe)
			result = Collections.synchronizedSet(new HashSet<Set<T>>());
		else
			result = new HashSet<Set<T>>();
	}

	@Override
	public void add(Set<T> t) {
		result.add(t);
	}

	@Override
	public Set<T> result() {
		return new SetFromSets<T>(result);
	}

}
