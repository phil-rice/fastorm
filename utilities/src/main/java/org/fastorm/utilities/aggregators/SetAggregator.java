package org.fastorm.utilities.aggregators;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetAggregator<T> implements IAggregator<T, Set<T>> {

	private Set<T> result;

	public SetAggregator(boolean threadSafe) {
		if (threadSafe)
			result = Collections.synchronizedSet(new HashSet<T>());
		else
			result = new HashSet<T>();
	}

	@Override
	public void add(T t) {
		result.add(t);
	}

	@Override
	public Set<T> result() {
		return result;
	}

}
