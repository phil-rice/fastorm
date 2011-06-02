package org.fastorm.utilities.strings;

public abstract class AbstractSimpleString implements ISimpleStringWithSetters {
	protected int start;
	protected int size;

	@Override
	public int length() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void setFromString(String string) {
		start = 0;
		size = string.length();
		setFromByteArray(string.getBytes(), start, size);
	}
}
