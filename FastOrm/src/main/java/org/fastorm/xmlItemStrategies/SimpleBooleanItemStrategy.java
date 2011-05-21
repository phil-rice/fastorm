package org.fastorm.xmlItemStrategies;

public class SimpleBooleanItemStrategy extends AbstractSimpleItemStrategy<Boolean> {

	public SimpleBooleanItemStrategy() {
		super();
	}

	public SimpleBooleanItemStrategy(Boolean defaultValue, boolean mandatory) {
		super(defaultValue, mandatory);
	}

	@Override
	Boolean fromString(String raw) {
		return Boolean.valueOf(raw);
	}

}
