package org.fastorm.utilities.pooling;

import java.util.concurrent.atomic.AtomicInteger;


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
	protected int makeNewObjectIndex() {
		int objectId = next.getAndIncrement();
		return objectId;
	}

}
