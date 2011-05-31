package org.fastorm.utilities.strings;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import org.fastorm.utilities.constants.UtilityConstants;
import org.fastorm.utilities.constants.UtilityMessages;
import org.fastorm.utilities.exceptions.WrappedException;

public class SimpleString implements ISimpleString {

	private int length;
	private final byte[] data;

	public SimpleString(int maxLength) {
		this.data = new byte[maxLength];
	}

	public SimpleString(String string) {
		data = string.getBytes();
		length = data.length;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public byte byteAt(int offset) {
		return data[offset];
	}

	public void setLength(int length) {
		if (length < 0 || length > data.length)
			throw new IllegalArgumentException(MessageFormat.format(UtilityMessages.cannotSetLength, length, data.length));
		this.length = length;

	}

	public void setBytes(byte[] bytes) {
		if (bytes.length > data.length)
			throw new IllegalArgumentException(MessageFormat.format(UtilityMessages.tooManyBytes, bytes.length, data.length));
		System.arraycopy(bytes, 0, data, 0, bytes.length);
	}

	public void setBytesAndLength(byte[] bytes, int from, int to) {
		int newLength = to - from + 1;
		setLength(newLength);
		System.arraycopy(bytes, from, data, 0, newLength);
	}

	public void setBytesAndLength(ISimpleString string, int from, int to) {
		SimpleString oldString = (SimpleString) string;
		int newLength = to - from + 1;
		setLength(newLength);
		System.arraycopy(oldString.data, from, data, 0, newLength);
	}

	@Override
	public String toString() {
		try {
			return "SimpleString [length=" + length + ", data=" + new String(data, 0, length, UtilityConstants.defaultStringCoding) + "]";
		} catch (UnsupportedEncodingException e) {
			throw WrappedException.wrap(e);
		}
	}

}
