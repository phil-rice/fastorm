package org.fastorm.utilities.functions;

public interface IFoldFunction<T, Acc> {

	Acc apply(T value, Acc initial);

}
