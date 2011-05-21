package org.fastorm.utilities;

import java.util.List;

public interface ISimpleList<T> {
	int size();

	T get(int index);

	List<T> getList();

}
