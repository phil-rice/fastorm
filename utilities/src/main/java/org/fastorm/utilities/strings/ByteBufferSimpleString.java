package org.fastorm.utilities.strings;

import java.nio.ByteBuffer;


public class ByteBufferSimpleString implements ISimpleString {

	private ByteBuffer byteBuffer;
	private int start;
	private int size;

	public ByteBufferSimpleString() {
	}

	public ByteBufferSimpleString(ByteBuffer buffer, int start, int size) {
		this.byteBuffer = buffer;
		this.start = start;
		this.size = size;
	}

	@Override
	public byte byteAt(int offset) {
		return byteBuffer.get(start + offset);
	}

	@Override
	public int length() {
		return size;
	}

	public void set(ByteBuffer byteBuffer, int start, int size) {
		this.byteBuffer = byteBuffer;
		this.start = start;
		this.size = size;
	}

}
