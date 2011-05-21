package org.fastorm.xmlItemStrategies;

public class SimpleStringItemStrategy extends AbstractSimpleItemStrategy<String> {

	public SimpleStringItemStrategy() {
		super();
	}

	public SimpleStringItemStrategy(String defaultValue, boolean mandatory) {
		super(defaultValue, mandatory);
	}

	@Override
	String fromString(String raw) {
		return raw;
	}

}
