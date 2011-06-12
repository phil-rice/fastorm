package org.fastorm.utilities.pooling;


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
	protected int makeNewObjectIndex() {
		return next++;
	}

}
