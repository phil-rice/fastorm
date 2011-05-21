package org.fastorm.utilities;

public interface IFunction1<From, To> {

	To apply(From from) throws Exception;
}
