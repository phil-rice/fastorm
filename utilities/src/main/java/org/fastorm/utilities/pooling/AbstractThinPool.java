package org.fastorm.utilities.pooling;

import java.lang.reflect.Array;
import java.text.MessageFormat;

import org.fastorm.utilities.constants.UtilityMessages;

public abstract class AbstractThinPool<T> implements IPoolThin<T> {
	abstract protected int makeNewObjectIndex();

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
		int objectIndex = makeNewObjectIndex();
		if (objectIndex >= data.length)
			throw new IllegalStateException(MessageFormat.format(UtilityMessages.objectPoolFull, objectIndex, data.length, defn));
		if (data[objectIndex] == null)
			data[objectIndex] = defn.createBlank();
		else if (poolOptions.cleanWhenReuse)
			defn.clean(data[objectIndex]);
		return objectIndex;
	}

	@Override
	public PoolOptions getPoolOptions() {
		return poolOptions;
	}

	@Override
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
