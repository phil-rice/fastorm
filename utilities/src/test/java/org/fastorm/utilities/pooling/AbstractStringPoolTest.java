package org.fastorm.utilities.pooling;

import org.fastorm.utilities.pooling.IPool;
import org.fastorm.utilities.strings.ISimpleStringWithSetters;

public abstract class AbstractStringPoolTest<T extends ISimpleStringWithSetters> extends AbstractPoolTest<T> {
	@Override
	public void testMakesObjects() {
		IPool<T> pool = makePool();
		T object = pool.newObject();
		assertEquals(0, object.length());
	}

	@Override
	protected IPoolCleanTestCallback<T> makeCleanTestCallback() {
		return new IPoolCleanTestCallback<T>() {

			@Override
			public void setData(T item) {
				item.setFromString("abc");
			}

			@Override
			public void checkDataBlank(T item) {
				assertEquals(0, item.length());
			}

			@Override
			public void checkDataHasBeenSet(T item) {
				assertEquals("abc", item.asString());
			}
		};
	}

}
