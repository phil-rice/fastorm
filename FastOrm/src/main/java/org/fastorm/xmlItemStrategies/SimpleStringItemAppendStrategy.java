package org.fastorm.xmlItemStrategies;

import java.util.Map;

public class SimpleStringItemAppendStrategy extends SimpleStringItemStrategy {

	private String otherKey;
	private String postFix;

	@Override
	public String defaultValue(String parentElementName, Map<String, ? extends Object> existing) {
		assert otherKey != null;
		assert postFix != null;
		return existing.get(otherKey) + postFix;
	}

	@Override
	public boolean useDefault() {
		return true;
	}

	public void setOtherKey(String otherKey) {
		this.otherKey = otherKey;
	}

	public void setPostFix(String postFix) {
		this.postFix = postFix;
	}
}
