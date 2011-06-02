package org.fastorm.utilities.strings;

public class ByteArraySimpleString extends AbstractSimpleString {

	private byte[] byteArray;
	private int start;
	private int size;

	public ByteArraySimpleString(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	@Override
	public byte byteAt(int offset) {
		return byteArray[start + offset];
	}

	@Override
	public void setFromByteArray(byte[] byteArray, int start, int size) {
		for (int i = 0; i < size; i++)
			this.byteArray[i] = byteArray[start + i];
		this.start = 0;
		this.size = size;
	}

	@Override
	public String asString() {
		return new String(byteArray, start, size);
	}

}
