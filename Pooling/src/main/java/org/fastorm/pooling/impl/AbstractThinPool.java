package org.fastorm.pooling.impl;

import java.lang.reflect.Array;

import org.fastorm.pooling.api.IObjectDefinition;
import org.fastorm.pooling.api.IPoolThin;
import org.fastorm.pooling.api.PoolOptions;

public abstract class AbstractThinPool<T> implements IPoolThin<T> {
	abstract protected int makeNewObjectId();

	private final T[] data;
	private final PoolOptions poolOptions;
	private final IObjectDefinition<T> defn;

	@SuppressWarnings("unchecked")
	public AbstractThinPool(PoolOptions poolOptions, IObjectDefinition<T> defn) {
		this.poolOptions = poolOptions;
		this.defn = defn;
		data = (T[]) Array.newInstance(defn.objectClass(), poolOptions.maxObjects);
	}

	@Override
	public IObjectDefinition<T> getDefinition() {
		return defn;
	}

	@Override
	public int newObjectId() {
		int objectId = makeNewObjectId();
		if (data[objectId] == null)
			data[objectId] = defn.createBlank();
		else if (poolOptions.cleanWhenReuse)
			defn.clean(data[objectId]);
		return objectId;
	}

	@Override
	public PoolOptions getPoolOptions() {
		return poolOptions;
	}

	public int size() {
		return 0;
	}

	protected void setObject(int i, T object) {
		data[i] = object;
	}

	@Override
	public T getObject(int objectId) {
		return data[objectId];
	}

}
