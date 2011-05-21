package org.fastorm.xmlToMap;

import java.text.MessageFormat;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.utilities.Lists;
import org.jdom.Attribute;
import org.jdom.Element;

public interface IUnrecognisedStrategy<K, V> {

	V processUnrecognisedAttribute(Attribute attribute, XmlToMapStrategy<K, V> xmlToMapStrategy);

	V processUnrecognisedElement(Element element, XmlToMapStrategy<K, V> xmlToMapStrategy);

	static class Utils {
		public static <K, V> CannotParseException makeException(XmlToMapStrategy<K, V> xmlToMapStrategy, String name) {
			throw new CannotParseException(MessageFormat.format(FastOrmMessages.illegalKey, name, Lists.sort(xmlToMapStrategy.idToStrategy.keySet())));
		}
	}
}
