package org.fastorm.xmlToMap;

import org.jdom.Attribute;
import org.jdom.Element;

public class NothingUnrecognised<K, V> implements IUnrecognisedStrategy<K, V> {

	public V processUnrecognisedAttribute(Attribute attribute, XmlToMapStrategy<K, V> xmlToMapStrategy) {
		throw Utils.makeException(xmlToMapStrategy, attribute.getName());
	}

	public V processUnrecognisedElement(Element element, XmlToMapStrategy<K, V> xmlToMapStrategy) {
		throw Utils.makeException(xmlToMapStrategy, element.getName());
	}

}
