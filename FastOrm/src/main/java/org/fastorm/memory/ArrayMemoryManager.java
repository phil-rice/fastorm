package org.fastorm.memory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.utilities.maps.Maps;

public class ArrayMemoryManager implements IMemoryManager {

	private final int size;
	private Map<Integer, Data> array = Maps.newMap();

	static class Data {
		private AtomicInteger index = new AtomicInteger();
		private Object[][] data;
		private final int length;

		public Data(int size, int length) {
			this.length = length;
			data = new Object[size][];
		}

		public void add(Object[] objects) {
			// System.out.println("Returning: " + size);
			data[index.getAndIncrement()] = objects;
		}

		public Object[] next() {
			// System.out.println("Allocating: " + size);
			return new Object[length];
		}
	}

	public ArrayMemoryManager() {
		this(10000000);
	}

	public ArrayMemoryManager(int size) {
		this.size = size;
	}

	@Override
	public void finishedWith(Object[] objects) {
		Data data = getData(objects.length);
		data.add(objects);

	}

	private Data getData(int length) {
		Data data = array.get(length);
		if (data == null) {
			data = new Data(size, length);
			array.put(length, data);
		}
		return data;
	}

	@Override
	public Object[] array(int length) {
		Data data = getData(length);
		return data.next();
	}

}
