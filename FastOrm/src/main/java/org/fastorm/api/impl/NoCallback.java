package org.fastorm.api.impl;

import org.fastorm.api.ICallback;

public class NoCallback <T> implements ICallback<T> {

	@Override
	public void process(T t) throws Exception {
	}

}
