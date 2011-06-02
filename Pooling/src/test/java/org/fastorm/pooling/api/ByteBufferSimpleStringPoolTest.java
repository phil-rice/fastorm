package org.fastorm.pooling.api;

import org.fastorm.utilities.strings.ByteBufferSimpleString;
import org.fastorm.utilities.strings.ISimpleStringWithSetters;

public class ByteBufferSimpleStringPoolTest extends AbstractStringPoolTest<ISimpleStringWithSetters>{

	@Override
	protected IPool<ISimpleStringWithSetters> makePool(PoolOptions poolOptions) {
		return IPool.Utils.makeArrayStringPool(poolOptions, 20);
	}

	@Override
	protected Class<ByteBufferSimpleString> classBeingTested() {
		return ByteBufferSimpleString.class;
	}


}
