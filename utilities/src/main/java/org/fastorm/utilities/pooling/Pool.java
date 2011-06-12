package org.fastorm.utilities.pooling;


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
