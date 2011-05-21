package org.fastorm.xmlItemStrategies;

import java.util.Map;

public class SimpleStringUseOtherValueItemStrategy extends SimpleStringItemStrategy {
	private Object key;

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	@Override
	public String defaultValue(String parentElementName, Map<String, ? extends Object> existing) {
		return existing.get(key).toString();
	}

	@Override
	public boolean useDefault() {
		return true;
	}

}
