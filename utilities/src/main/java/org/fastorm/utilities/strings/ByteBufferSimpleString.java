package org.fastorm.utilities.strings;

import java.nio.ByteBuffer;

public class ByteBufferSimpleString extends AbstractSimpleString {

	private ByteBuffer byteBuffer;

	public ByteBufferSimpleString(ByteBuffer buffer) {
		this.byteBuffer = buffer;
	}

	@Override
	public byte byteAt(int offset) {
		return byteBuffer.get(start + offset);
	}

	@Override
	public int length() {
		return size;
	}

	@Override
	public void setFromByteArray(byte[] byteArray, int start, int size) {
		for (int i = 0; i < start; i++)
			byteBuffer.put(byteArray, start, size);
	}

	@Override
	public String asString() {
		return new String(byteBuffer.array(), start, size);
	}

}
