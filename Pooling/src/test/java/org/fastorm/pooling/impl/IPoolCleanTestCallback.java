package org.fastorm.pooling.impl;

public interface IPoolCleanTestCallback<T> {
	void setData(T item);

	void checkDataBlank(T item);

	void checkDataHasBeenSet(T item);
}
