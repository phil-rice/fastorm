package org.fastorm.pooling.impl;

import java.io.UnsupportedEncodingException;

import org.fastorm.pooling.api.IPoolThin;
import org.fastorm.pooling.api.IStringPool;
import org.fastorm.utilities.constants.UtilityConstants;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.strings.ISimpleString;
import org.fastorm.utilities.strings.SimpleString;

public class StringPool extends Pool<ISimpleString> implements IStringPool {

	public StringPool(IPoolThin<ISimpleString> poolThin) {
		super(poolThin);
	}

	@Override
	public ISimpleString newString(String from) {
		try {
			byte[] bytes = from.getBytes(UtilityConstants.defaultStringCoding);
			return newString(bytes, 0, bytes.length - 1);
		} catch (UnsupportedEncodingException e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public ISimpleString newString(ISimpleString string, int from, int to) {
		SimpleString newObject = (SimpleString) newObject();
		newObject.setBytesAndLength(string, from, to);
		return newObject;
	}

	@Override
	public ISimpleString newString(byte[] bytes, int from, int to) {
		SimpleString newObject = (SimpleString) newObject();
		newObject.setBytesAndLength(bytes, from, to);
		return newObject;
	}

}
