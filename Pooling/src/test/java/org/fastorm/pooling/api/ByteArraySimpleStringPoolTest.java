package org.fastorm.pooling.api;

import org.fastorm.utilities.strings.ByteArraySimpleString;
import org.fastorm.utilities.strings.ISimpleStringWithSetters;

public class ByteArraySimpleStringPoolTest extends AbstractStringPoolTest<ISimpleStringWithSetters> {

	@Override
	protected IPool<ISimpleStringWithSetters> makePool(PoolOptions poolOptions) {
		return IPool.Utils.makeArrayStringPool(poolOptions, 20);
	}

	@Override
	protected Class<ByteArraySimpleString> classBeingTested() {
		return ByteArraySimpleString.class;
	}

}
