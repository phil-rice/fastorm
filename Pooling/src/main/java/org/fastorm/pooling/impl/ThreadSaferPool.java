package org.fastorm.pooling.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.pooling.api.IObjectDefinition;
import org.fastorm.pooling.api.PoolOptions;

public class ThreadSaferPool<T> extends AbstractThinPool<T> {
	private final AtomicInteger next = new AtomicInteger();

	public ThreadSaferPool(PoolOptions poolOptions, IObjectDefinition<T> defn) {
		super(poolOptions, defn);
	}

	@Override
	public void dispose() {
		next.set(0);
	}

	@Override
	protected int makeNewObjectId() {
		int objectId = next.getAndIncrement();
		return objectId;
	}

}
