package org.fastorm.utilities.foldFunctions;

import org.fastorm.utilities.IAggregateFunction;
import org.fastorm.utilities.IFoldFunction;

public interface ISymmetricFunction<T> extends IFoldFunction<T, T>, IAggregateFunction<T> {

}