package org.fastorm.xmlToMap;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.Functions;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;
import org.jdom.Attribute;
import org.jdom.Element;

public class XmlToMapStrategy<K, V> implements IStrategyNode<K, V> {

	/** What do I do with this element */
	public final Map<String, IStrategyNode<K, V>> idToStrategy;
	/** Turn the element into a V */
	public final XmlToMapParser<K, V> parser;
	/** Turn any map into a V */
	public final IFunction1<Map<K, V>, V> mapToValueFn;
	/** Turn strings into V */
	public final IFunction1<String, K> transformId;
	private final IUnrecognisedStrategy<K, V> unrecognisedStrategy;

	public XmlToMapStrategy(Map<String, IStrategyNode<K, V>> idToStrategy, IUnrecognisedStrategy<K, V> unrecognisedStrategy) {
		this(idToStrategy, unrecognisedStrategy, Functions.<Map<K, V>, V> identity(), Functions.<String, K> identity());
	}

	public XmlToMapStrategy(Map<String, IStrategyNode<K, V>> idToStrategy, IUnrecognisedStrategy<K, V> unrecognisedStrategy, IFunction1<Map<K, V>, V> mapToValueFn, IFunction1<String, K> transformId) {
		this.unrecognisedStrategy = unrecognisedStrategy;
		this.mapToValueFn = mapToValueFn;
		this.transformId = transformId;
		this.parser = new XmlToMapParser<K, V>(this);
		this.idToStrategy = Collections.unmodifiableMap(new LinkedHashMap<String, IStrategyNode<K, V>>(idToStrategy));
	}

	@Override
	@SuppressWarnings("unchecked")
	public V transform(Element element) {
		try {
			Map<K, V> map = Maps.newMap();
			for (Element child : (List<Element>) element.getChildren()) {
				String name = child.getName();
				K key = transformId.apply(name);
				IStrategyNode<K, V> strategyNode = idToStrategy.get(name);
				V value = strategyNode == null ? unrecognisedStrategy.processUnrecognisedElement(child, this) : strategyNode.transform(child);
				map.put(key, value);
			}
			for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
				String name = attribute.getName();
				K key = transformId.apply(name);
				IStrategyNode<K, V> strategyNode = idToStrategy.get(name);
				if (strategyNode == null)
					unrecognisedStrategy.processUnrecognisedAttribute(attribute, this);
				V value = strategyNode.transform(attribute);
				map.put(key, value);
			}
			for (Entry<String, IStrategyNode<K, V>> entry : idToStrategy.entrySet()) {
				String keyName = entry.getKey();
				K key = transformId.apply(keyName);
				if (!map.containsKey(key)) {
					IStrategyNode<K, V> strategyNode = entry.getValue();
					if (strategyNode.isMandatory())
						throw new CannotHaveDefaultFor(MessageFormat.format(FastOrmMessages.cannotHaveDefaultFor, keyName));
					if (strategyNode.useDefault()) {
						V value = strategyNode.defaultValue(element.getName(), map);
						map.put(key, value);
					}
				}
			}
			V result = mapToValueFn.apply(map);
			return result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}

	}

	@Override
	public V transform(Attribute attribute) {
		throw new CannotParseException(MessageFormat.format(FastOrmMessages.thisKeyCannotBeAnAttribute, attribute.getName()));
	}

	@Override
	public V defaultValue(String parentElementName, Map<K, ? extends V> existing) {
		return null;
	}

	@Override
	public boolean isMandatory() {
		return false;
	}

	@Override
	public boolean useDefault() {
		return false;
	}

}
