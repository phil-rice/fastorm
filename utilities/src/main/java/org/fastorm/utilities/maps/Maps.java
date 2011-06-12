package org.fastorm.utilities.maps;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.fastorm.utilities.collections.Lists;
import org.fastorm.utilities.constants.UtilityMessages;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.Functions;
import org.fastorm.utilities.functions.IFunction1;

public class Maps {

	public static <KV> KV[] toArray(final Map<? extends KV, ? extends KV> map, KV[] baseArray) {
		List<KV> result = new ArrayList<KV>();
		for (Entry<? extends KV, ? extends KV> entry : map.entrySet()) {
			result.add(entry.getKey());
			result.add(entry.getValue());
		}
		return result.toArray(baseArray);
	}

	public static <K, V> Map<K, V> makeMap(Object... attributesAndValues) {
		Map<K, V> result = new HashMap<K, V>(attributesAndValues.length / 2);
		return putInto(result, attributesAndValues);
	}

	public static <K, V> Map<K, V> makeImmutableMap(Object... attributesAndValues) {
		Map<K, V> result = new HashMap<K, V>(attributesAndValues.length / 2);
		return Collections.unmodifiableMap(putInto(result, attributesAndValues));
	}

	public static <K, V> Map<K, V> makeLinkedMap(Object... attributesAndValues) {
		Map<K, V> result = new LinkedHashMap<K, V>(attributesAndValues.length / 2);
		return putInto(result, attributesAndValues);
	}

	public static <K, V> Map<K, V> merge(Map<K, V>... maps) {
		Map<K, V> result = new HashMap<K, V>();
		for (Map<K, V> map : maps)
			result.putAll(map);
		return result;
	}

	public static <K, V> Map<K, V> merge(Iterable<Map<K, V>> maps) {
		Map<K, V> result = new HashMap<K, V>();
		for (Map<K, V> map : maps)
			result.putAll(map);
		return result;
	}

	public static <K1, K2, V> void addToMapOfLinkedMaps(Map<K1, Map<K2, V>> map, K1 key1, K2 key2, V value) {
		addToMapOfMaps(map, LinkedHashMap.class, key1, key2, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K1, K2, V> void addToMapOfMaps(Map<K1, Map<K2, V>> map, Class<? extends Map> nestedMapClass, K1 key1, K2 key2, V value) {
		try {
			Map<K2, V> map1 = map.get(key1);
			if (map1 == null) {
				map1 = nestedMapClass.newInstance();
				map.put(key1, map1);
			}
			map1.put(key2, value);
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public static <K, V> void addToList(Map<K, List<V>> map, K key, V value) {
		Maps.addToCollection(map, ArrayList.class, key, value);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K, V, ColV extends Collection<V>> void addToCollection(Map<K, ColV> map, Class colClass, K key, V value) {
		try {
			ColV existing = map.get(key);
			if (existing == null) {
				existing = (ColV) colClass.getConstructor().newInstance();
				map.put(key, existing);
			}
			existing.add(value);
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <K, V> Map<K, V> putInto(Map<K, V> result, Object... attributesAndValues) {
		List<Object>[] keysAndValues = Lists.partition(2, Arrays.asList(attributesAndValues));
		Iterator<Object> keys = keysAndValues[0].iterator();
		Iterator<Object> values = keysAndValues[1].iterator();
		while (keys.hasNext() && values.hasNext()) {
			K key = (K) keys.next();
			V value = (V) values.next();
			if (result.containsKey(key))
				throw new IllegalArgumentException(MessageFormat.format(UtilityMessages.duplicateKey, key, result.get(key), value));
			result.put(key, value);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static <K, V> Map<Class, Map<K, V>> partitionByValueClass(Map<K, V> input, Class<? extends Map> nestedMapClass, Class... partitionClasses) {
		List<Class> classList = Arrays.asList(partitionClasses);
		Map<Class, Map<K, V>> result = Maps.newMap();
		for (Entry<K, V> entry : input.entrySet()) {
			V value = entry.getValue();
			int index = Lists.findClass(classList, value);
			if (index == -1)
				throw new IllegalArgumentException("Key of " + entry.getKey() + " has a Value class of " + value.getClass() + " found, which is not in " + classList);
			Class key = classList.get(index);
			Maps.addToMapOfMaps(result, nestedMapClass, key, entry.getKey(), value);
		}
		return result;
	}

	public static <K, V, P> Map<P, Map<K, V>> partitionByKey(Map<K, V> input, IFunction1<K, P> partitionFunction) {
		try {
			Map<P, Map<K, V>> result = Maps.newMap();
			for (Entry<K, V> entry : input.entrySet()) {
				K key = entry.getKey();
				P partition = partitionFunction.apply(key);
				V value = entry.getValue();
				Maps.addToMapOfLinkedMaps(result, partition, key, value);
			}
			return result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public static <K, V, P> Map<P, Map<K, V>> partitionByValue(Map<K, V> input, IFunction1<V, P> partitionFunction) {
		try {
			Map<P, Map<K, V>> result = Maps.newMap();
			for (Entry<K, V> entry : input.entrySet()) {
				K key = entry.getKey();
				V value = entry.getValue();
				P partition = partitionFunction.apply(value);
				Maps.addToMapOfLinkedMaps(result, partition, key, value);
			}
			return result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public static <K, V> Map<K, V> newMap() {
		return new HashMap<K, V>();
	}

	public static <K, V> IFunction1<K, V> get(final Map<K, V> map) {
		return new IFunction1<K, V>() {
			@Override
			public V apply(K from) throws Exception {
				return map.get(from);
			}
		};
	}

	public static <K, V> IFunction1<Entry<String, String>, String> entryToStr(final String pattern) {
		return new IFunction1<Map.Entry<String, String>, String>() {
			@Override
			public String apply(Entry<String, String> from) throws Exception {
				return MessageFormat.format(pattern, from.getKey(), from.getValue());
			}
		};
	}

	/** The map is from K to a pattern. The parameters passed to MessageFormat are key + extraParameters */
	public static <K, V> IFunction1<K, String> keyToValuePatternToStr(final Map<K, V> map, final Object... extraParameters) {
		return new IFunction1<K, String>() {
			@Override
			public String apply(K from) throws Exception {
				List<Object> arguments = new ArrayList<Object>();
				arguments.add(from);
				arguments.addAll(Arrays.asList(extraParameters));
				String pattern = map.get(from).toString();
				return MessageFormat.format(pattern, arguments.toArray());
			}
		};
	}

	public static <K, From, To> Map<K, To> mapTheMap(Map<K, From> map, IFunction1<From, To> mapValue) {
		return mapTheMap(map, Functions.<K, K> identity(), mapValue);
	}

	public static <FromK, ToK, FromV, ToV> Map<ToK, ToV> mapTheMap(Map<FromK, FromV> map, IFunction1<FromK, ToK> mapKey, IFunction1<FromV, ToV> mapValue) {
		try {
			Map<ToK, ToV> result = new HashMap<ToK, ToV>();
			for (Entry<FromK, FromV> entry : map.entrySet())
				result.put(mapKey.apply(entry.getKey()), mapValue.apply(entry.getValue()));
			return result;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public static <K, V> Map<K, V> fromSimpleMap(ISimpleMap<K, V> map) {
		Map<K, V> result = new HashMap<K, V>();
		for (K key : map.keys())
			result.put(key, map.get(key));
		return result;
	}

	public static <K, V> V findOrCreate(Map<K, V> map, K key, Callable<V> callable) {
		try {
			if (map.containsKey(key))
				return map.get(key);
			V v = callable.call();
			map.put(key, v);
			return v;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public static <K> void add(Map<K, Integer> map, K key, int size) {
		map.put(key, size + intFor(map, key));
	}

	public static <K> int intFor(Map<K, Integer> map, K key) {
		Integer existing = map.get(key);
		return existing == null ? 0 : existing;
	}

	public static <K, V> Map<K, V> with(Map<K, V> map, K key, V value) {
		Map<K, V> newMap = copyMap(map);
		newMap.put(key, value);
		return newMap;
	}

	public static <K, V> Map<K, V> copyMap(Map<K, V> map) {
		Map<K, V> newMap = newMapOfSameTypeMap(map);
		newMap.putAll(map);
		return newMap;
	}

	public static <K, V> Map<K, V> newMapOfSameTypeMap(Map<K, V> map) {
		return newMap(map.getClass());
	}

	public static <K, V> Map<K, V> newMap(Class<?> clazz) {
		if (LinkedHashMap.class.isAssignableFrom(clazz))
			return new LinkedHashMap<K, V>();
		else if (HashMap.class.isAssignableFrom(clazz))
			return new HashMap<K, V>();
		else if (IdentityHashMap.class.isAssignableFrom(clazz))
			return new IdentityHashMap<K, V>();
		throw new IllegalArgumentException();
	}
}
