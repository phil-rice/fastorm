package org.fastorm.utilities.pooling;

import org.fastorm.utilities.pooling.IPool;
import org.fastorm.utilities.pooling.PoolOptions;

public class ThreadSafePoolTest extends PoolTest {

	@Override
	protected IPool<IExampleForPool> makePool(boolean cleanWhenReuse) {
		return IPool.Utils.pool(new PoolOptions().withCleanWhenReuse(cleanWhenReuse), defn);
	}

}
