package org.fastorm.utilities.strings;


public class ByteArraySimpleString implements ISimpleString {

	private byte[] byteArray;
	private int start;
	private int size;

	@Override
	public int length() {
		return size;
	}

	@Override
	public byte byteAt(int offset) {
		return byteArray[start + offset];
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void set(byte[] byteArray, int start, int size) {
		this.byteArray = byteArray;
		this.start = start;
		this.size = size;
	}
}
