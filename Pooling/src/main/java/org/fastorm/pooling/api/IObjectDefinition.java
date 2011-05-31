package org.fastorm.pooling.api;

public interface IObjectDefinition<T> {

	Class<T> objectClass();

	T createBlank();

	void clean(T oldObject);

}
