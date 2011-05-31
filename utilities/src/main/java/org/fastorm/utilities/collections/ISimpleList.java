package org.fastorm.utilities.collections;

import java.util.List;

public interface ISimpleList<T> {
	int size();

	T get(int index);

	List<T> slowList();

}
