package org.fastorm.utilities.maps;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.fastorm.utilities.constants.UtilityMessages;

public class MapsTest extends TestCase {

	public void testMakeMap() {
		checkMakeMap();
		checkMakeMap("a", 1);
		checkMakeMap("a", 1, "b", 2);

		try {
			Maps.makeMap("a", 1, "a", 2);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(MessageFormat.format(UtilityMessages.duplicateKey, "a", "1", "2"), e.getMessage());
		}
	}

	public void testNewMap() {
		Map<Object, Object> map = Maps.newMap();
		assertEquals(0, map.size());
		map.put("a", 1);
		assertEquals(1, map.get("a"));
	}

	private void checkMakeMap(Object... expected) {
		Map<Object, Object> actual = Maps.makeMap(expected);
		assertEquals(expected.length / 2, actual.size());
		for (int i = 0; i < expected.length; i += 2) {
			Object key = expected[i + 0];
			Object value = expected[i + 1];
			assertEquals(value, actual.get(key));
		}
	}

	public void testAddToMap() {
		Map<Object, List<Object>> map = Maps.newMap();
		Maps.addToList(map, "a", 1);
		Maps.addToList(map, "a", 2);
		Maps.addToList(map, "a", 3);
		Maps.addToList(map, "b", 4);
		Maps.addToList(map, "b", 5);
		assertEquals(Maps.makeMap("a", Arrays.asList(1, 2, 3), "b", Arrays.asList(4, 5)), map);
	}

	public void testPartitionMapByValueClass() {
		checkPartitionMapByValueClass(Maps.makeMap("a", 1, "b", 2, "c", "3", "d", "4"), //
				Maps.makeMap(Integer.class, Maps.makeMap("a", 1, "b", 2), String.class, Maps.makeMap("c", "3", "d", "4")),//
				Integer.class, String.class);
		checkPartitionMapByValueClass(Maps.makeMap("a", 1, "b", 2, "c", "3", "d", "4"), //
				Maps.makeMap(Integer.class, Maps.makeMap("a", 1, "b", 2), Object.class, Maps.makeMap("c", "3", "d", "4")),//
				Integer.class, Object.class);
	}

	public void testNewMapWithClassParameter() {
		checkNewMap(HashMap.class);
		checkNewMap(IdentityHashMap.class);
		checkNewMap(LinkedHashMap.class);
	}

	private void checkNewMap(Class<?> clazz) {
		Map<Object, Object> map = Maps.newMap(clazz);
		assertTrue(clazz.isAssignableFrom(map.getClass()));
		assertEquals(0, map.size());
	}

	public void testNewMapOfSameType() {
		checkNewMapOfSameType(HashMap.class);
		checkNewMapOfSameType(IdentityHashMap.class);
		checkNewMapOfSameType(LinkedHashMap.class);
	}

	private void checkNewMapOfSameType(Class<?> clazz) {
		Map<Object, Object> oldMap = Maps.newMap(clazz);
		Map<Object, Object> map = Maps.newMapOfSameTypeMap(oldMap);
		assertTrue(clazz.isAssignableFrom(map.getClass()));
		assertEquals(0, map.size());
	}

	public void testCopy() {
		checkCopy(HashMap.class);
		checkCopy(IdentityHashMap.class);
		checkCopy(LinkedHashMap.class);
	}

	private void checkCopy(Class<?> clazz) {
		Map<Object, Object> oldMap = Maps.newMap(clazz);
		oldMap.put("a", 1);
		oldMap.put("b", 2);
		Map<Object, Object> map = Maps.copyMap(oldMap);
		assertTrue(clazz.isAssignableFrom(map.getClass()));
		assertEquals(oldMap, map);
		assertNotSame(oldMap, map);
	}

	public void testWith() {
		checkWith(HashMap.class);
		checkWith(IdentityHashMap.class);
		checkWith(LinkedHashMap.class);
	}

	private void checkWith(Class<?> clazz) {
		Map<Object, Object> oldMap = Maps.newMap(clazz);
		oldMap.put("a", 1);
		oldMap.put("b", 2);
		Map<Object, Object> copyOfOld = Maps.copyMap(oldMap);
		Map<Object, Object> expected = Maps.copyMap(oldMap);
		expected.put("c", 3);

		Map<Object, Object> map = Maps.with(oldMap, "c", 3);
		assertTrue(clazz.isAssignableFrom(map.getClass()));
		assertEquals(copyOfOld, oldMap);
		assertEquals(expected, map);
	}

	@SuppressWarnings("rawtypes")
	private void checkPartitionMapByValueClass(Map<Object, Object> input, Map<Object, Object> expected, Class... partitionClasses) {
		Map<Class, Map<Object, Object>> actual = Maps.partitionByValueClass(input, LinkedHashMap.class, partitionClasses);
		assertEquals(expected, actual);

	}
}
