package org.fastorm.stats;

import java.util.List;

import org.fastorm.api.IJob;

public abstract class SpecStage<T> implements ISpecStage {

	private final List<T> values;

	public SpecStage(List<T> values) {
		this.values = values;
	}

	abstract protected IJob transform(IJob container, T t) throws Exception;

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public IJob makeFastOrm(final IJob initial, int index) throws Exception {
		if (index >= values.size())
			return null;
		return transform(initial, values.get(index));
	}

}
