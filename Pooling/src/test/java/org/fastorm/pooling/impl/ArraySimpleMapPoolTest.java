package org.fastorm.pooling.impl;

import java.util.Arrays;
import java.util.List;

import org.fastorm.pooling.api.IPool;
import org.fastorm.pooling.api.PoolOptions;
import org.fastorm.utilities.maps.ArraySimpleMap;

public class ArraySimpleMapPoolTest extends AbstractPoolTest<ArraySimpleMap<String, String>>  {

	private String key1 = "k1";
	private String key2 = "k2";
	private String key3 = "k3";
	private List<String> keys = Arrays.asList(key1, key2, key3);
	private List<String> values = Arrays.asList("v1", "v2", "v3");

	@Override
	protected IPool<ArraySimpleMap<String, String>> makePool(PoolOptions poolOptions) {
		return IPool.Utils.makeArraySimpleMapPool(poolOptions, keys, String.class);
	}

	@Override
	protected IPoolCleanTestCallback<ArraySimpleMap<String, String>> makeCleanTestCallback() {
		return new IPoolCleanTestCallback<ArraySimpleMap<String,String>>() {
			@Override
			public void setData(ArraySimpleMap<String, String> item) {
				item.setValuesFrom(values);
			}
			
			@Override
			public void checkDataHasBeenSet(ArraySimpleMap<String, String> item) {
				assertEquals("v1", item.get(key1));
				assertEquals("v2", item.get(key2));
				assertEquals("v3", item.get(key3));
			}
			
			@Override
			public void checkDataBlank(ArraySimpleMap<String, String> item) {
				assertEquals(null, item.get(key1));
				assertEquals(null, item.get(key2));
				assertEquals(null, item.get(key3));
			}
		};
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Class<ArraySimpleMap<String, String>> classBeingTested() {
		return (Class)ArraySimpleMap.class;
	}
	
	
}
