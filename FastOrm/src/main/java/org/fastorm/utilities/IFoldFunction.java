package org.fastorm.utilities;

public interface IFoldFunction<T, Acc> {

	Acc apply(T value, Acc initial);

}
