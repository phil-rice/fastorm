package org.fastorm.xmlToMap;

import java.io.StringReader;
import java.util.Map;

import org.fastorm.utilities.WrappedException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XmlToMapParser<K, V> {

	private final XmlToMapStrategy<K, V> mapStrategy;

	public XmlToMapParser(XmlToMapStrategy<K, V> mapStrategy) {
		this.mapStrategy = mapStrategy;
	}

	@SuppressWarnings("unchecked")
	public Map<K, V> parse(String xml) {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(new StringReader(xml));
			Element rootElement = document.getRootElement();

			Object result = mapStrategy.transform(rootElement);
			assert result instanceof Map : "The 'V' must implement Map. It is of type" + result.getClass();
			return (Map<K, V>) result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

}