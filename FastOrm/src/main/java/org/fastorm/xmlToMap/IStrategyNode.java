package org.fastorm.xmlToMap;

import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Element;

public interface IStrategyNode<K, V> {

	V transform(Element element);

	V transform(Attribute attribute);

	/** Used if there is a key strategy specified but the value is not present */
	V defaultValue(String parentElementName, Map<K, ? extends V> existing);

	boolean isMandatory();

	boolean useDefault();

}
