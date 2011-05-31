package org.fastorm.pooling.impl;

import org.fastorm.pooling.api.IPool;
import org.fastorm.pooling.api.PoolOptions;
import org.fastorm.pooling.example.IExampleForPool;

public class ThreadSafePoolTest extends AbstractPoolTest {

	@Override
	protected IPool<IExampleForPool> makePool(boolean cleanWhenReuse) {
		return IPool.Utils.pool(new PoolOptions().withCleanWhenReuse(cleanWhenReuse), defn);
	}

}
