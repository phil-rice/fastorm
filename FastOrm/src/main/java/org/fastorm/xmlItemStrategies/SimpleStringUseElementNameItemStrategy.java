package org.fastorm.xmlItemStrategies;

import java.util.Map;

public class SimpleStringUseElementNameItemStrategy extends SimpleStringItemStrategy {

	@Override
	public String defaultValue(String parentElementName, Map<String, ? extends Object> existing) {
		return parentElementName;
	}

	@Override
	public boolean useDefault() {
		return true;
	}

}
