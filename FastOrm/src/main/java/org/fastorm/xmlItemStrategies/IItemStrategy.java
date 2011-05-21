package org.fastorm.xmlItemStrategies;

import java.util.Map;

import org.fastorm.xmlToMap.IStrategyNode;

public interface IItemStrategy<K, V> extends IStrategyNode<K, V> {

	V parse(Map<K, V> simpleMap, String raw);

}
