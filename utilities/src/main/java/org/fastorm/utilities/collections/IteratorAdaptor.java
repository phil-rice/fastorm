package org.fastorm.utilities.collections;

import java.util.Iterator;

public abstract class IteratorAdaptor<T> implements Iterator<T> {

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
