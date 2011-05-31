package org.fastorm.xmlItemStrategies;

import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class AbstractSimpleItemStrategy<T> implements IItemStrategy<String, Object> {
	abstract T fromString(String raw);

	private T defaultValue;
	private boolean mandatory;
	private boolean useDefault;

	public void setUseDefault(boolean useDefault) {
		this.useDefault = useDefault;
	}

	public AbstractSimpleItemStrategy() {
	}

	public AbstractSimpleItemStrategy(T defaultValue, boolean mandatory) {
		this.defaultValue = defaultValue;
		this.mandatory = mandatory;
		this.useDefault = defaultValue != null;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
		useDefault = true;
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String toString(Map<String, Object> simpleMap, String key) {
		return key;
	}

	@Override
	public Object parse(Map<String, Object> simpleMap, String raw) {
		return fromString(raw);
	}

	@Override
	public T transform(Element element) {
		return fromString(element.getText());
	}

	@Override
	public T transform(Attribute attribute) {
		return fromString(attribute.getValue());
	}

	@Override
	public T defaultValue(String parentElementName, Map<String, ? extends Object> existing) {
		return defaultValue;
	}

	@Override
	public boolean useDefault() {
		return useDefault;
	}

}
