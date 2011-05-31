package org.fastorm.pooling.impl;

import org.fastorm.pooling.api.IObjectDefinition;
import org.fastorm.pooling.api.PoolOptions;

public class ThreadUnsafePool<T> extends AbstractThinPool<T> {

	public ThreadUnsafePool(PoolOptions poolOptions, IObjectDefinition<T> defn) {
		super(poolOptions, defn);
	}

	private int next;

	@Override
	public void dispose() {
		next = 0;
	}

	@Override
	protected int makeNewObjectId() {
		return next++;
	}

}
