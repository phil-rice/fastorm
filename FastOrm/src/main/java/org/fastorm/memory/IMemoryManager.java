package org.fastorm.memory;

public interface IMemoryManager {
	void finishedWith(Object[] objects);

	Object[] array(int length);

}
