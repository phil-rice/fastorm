package org.fastorm.exceptions;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.fastorm.constants.FastOrmMessages;

public class IllegalKeyException extends RuntimeException {

	private final String key;
	private final List<String> keys;

	public IllegalKeyException(String key, List<String> keys) {
		super(MessageFormat.format(FastOrmMessages.illegalKey, key, keys));
		this.key = key;
		this.keys = Collections.unmodifiableList(keys);
	}

	public String getKey() {
		return key;
	}

	public List<String> getKeys() {
		return keys;
	}

}
