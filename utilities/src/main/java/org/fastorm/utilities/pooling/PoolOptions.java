package org.fastorm.utilities.pooling;

public class PoolOptions {

	public final boolean tryToBeThreadSafe;
	public final int maxObjects;
	public final boolean cleanWhenReuse;

	public PoolOptions(boolean tryToBeThreadSafe, int maxObjects, boolean cleanWhenReuse) {
		this.tryToBeThreadSafe = tryToBeThreadSafe;
		this.maxObjects = maxObjects;
		this.cleanWhenReuse = cleanWhenReuse;
	}

	public PoolOptions() {
		this(false, 1000, true);
	}

	public PoolOptions withCleanWhenReuse(boolean cleanWhenReuse) {
		return new PoolOptions(tryToBeThreadSafe, maxObjects, cleanWhenReuse);
	}

	public PoolOptions withMaxObjects(int maxObjects) {
		return new PoolOptions(tryToBeThreadSafe, maxObjects, cleanWhenReuse);
	}
}
