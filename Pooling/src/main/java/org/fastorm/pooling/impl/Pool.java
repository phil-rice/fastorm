package org.fastorm.pooling.impl;

import org.fastorm.pooling.api.IObjectDefinition;
import org.fastorm.pooling.api.IPool;
import org.fastorm.pooling.api.IPoolThin;
import org.fastorm.pooling.api.PoolOptions;

public class Pool<T> implements IPool<T> {
	private final IPoolThin<T> poolthin;

	public Pool(IPoolThin<T> poolThin) {
		poolthin = poolThin;
	}

	@Override
	public IObjectDefinition<T> getDefinition() {
		return poolthin.getDefinition();
	}

	@Override
	public void prepopulate() {

	}

	@Override
	public T newObject() {
		int objectId = newObjectId();
		return getObject(objectId);
	}

	@Override
	public PoolOptions getPoolOptions() {
		return poolthin.getPoolOptions();
	}

	@Override
	public int size() {
		return poolthin.size();
	}

	@Override
	public int newObjectId() {
		int objectId = poolthin.newObjectId();
		return objectId;
	}

	@Override
	public T getObject(int objectId) {
		return poolthin.getObject(objectId);
	}

	@Override
	public void dispose() {
		poolthin.dispose();
	}

}
