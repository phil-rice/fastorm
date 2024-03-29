package org.fastorm.utilities.callbacks;

import java.util.ArrayList;
import java.util.List;


public class MemoryCallback<T> implements ICallback<T> {
	private final List<T> result = new ArrayList<T>();

	@Override
	public void process(T t) throws Exception {
		result.add(t);
	}

	public List<T> getResult() {
		return result;
	}
}