package org.fastorm.stats;

import java.util.List;

import org.fastorm.api.IFastOrm;

public abstract class SpecStage<T> implements ISpecStage {

	private final List<T> values;

	public SpecStage(List<T> values) {
		this.values = values;
	}

	abstract protected IFastOrm transform(IFastOrm container, T t) throws Exception;

	public int size() {
		return values.size();
	}

	public IFastOrm makeFastOrm(final IFastOrm initial, int index) throws Exception {
		if (index >= values.size())
			return null;
		return transform(initial, values.get(index));
	}

}
