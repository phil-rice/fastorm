package org.fastorm.memory;


public class NoMemoryManager implements IMemoryManager {

	public Object[] array(int length) {
		return new Object[length];
	}

	@Override
	public void finishedWith(Object[] objects) {
	}

}
