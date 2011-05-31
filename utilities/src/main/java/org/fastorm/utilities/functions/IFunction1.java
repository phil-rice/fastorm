package org.fastorm.utilities.functions;

public interface IFunction1<From, To> {

	To apply(From from) throws Exception;
}
