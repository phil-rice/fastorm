package org.fastorm.utilities;

public interface IAggregateFunction<T> {

	T apply(T left, T right);

}
