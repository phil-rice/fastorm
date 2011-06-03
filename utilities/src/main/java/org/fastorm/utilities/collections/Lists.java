package org.fastorm.utilities.collections;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fastorm.utilities.constants.UtilityMessages;
import org.fastorm.utilities.maps.Maps;

public class Lists {

	public static <T> int indexOf(List<T> list, T object) {
		int indexOne = identityIndexOf(list, object);
		if (indexOne != -1)
			return indexOne;
		int indexTwo = equalsIndexOf(list, object);
		return indexTwo;
	}

	public static <T> int indexOf(List<T> listOne, List<T> listTwo, T object) {
		int indexOne = identityIndexOf(listOne, object);
		if (indexOne != -1)
			return indexOne;
		int indexTwo = identityIndexOf(listTwo, object);
		if (indexTwo != -1)
			return indexTwo + listOne.size();
		int indexOneA = equalsIndexOf(listOne, object);
		if (indexOneA != -1)
			return indexOneA;
		int indexTwoB = equalsIndexOf(listTwo, object);
		if (indexTwoB != -1)
			return indexTwoB + listOne.size();
		return -1;
	}

	private static <T> int equalsIndexOf(List<T> list, T object) {
		for (int i = 0; i < list.size(); i++)
			if (object.equals(list.get(i)))
				return i;
		return -1;
	}

	private static <T> int identityIndexOf(List<T> list, T object) {
		for (int i = 0; i < list.size(); i++)
			if (list.get(i) == object)
				return i;
		return -1;
	}

	private static <To, From> List<To> makeListFor(Iterable<From> from) {
		int size = from instanceof List ? ((List<From>) from).size() : 0;
		List<To> result = new ArrayList<To>(size);
		return result;
	}

	public static <T> List<T>[] partition(int num, List<T> input) {
		assert input.size() % num == 0;
		if (num <= 0)
			throw new IllegalArgumentException(MessageFormat.format(UtilityMessages.needPositivePartitionSize, num));
		int size = input.size() / num;
		@SuppressWarnings("unchecked")
		List<T>[] result = (List<T>[]) Array.newInstance(List.class, num);
		for (int i = 0; i < num; i++)
			result[i] = new ArrayList<T>(size);
		int i = 0;
		for (Iterator<T> iterator = input.iterator(); iterator.hasNext();)
			result[i++ % num].add(iterator.next());
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static <T> Map<Class, List<T>> partitionByClass(Iterable<T> input, Class... partitionClasses) {
		List<Class> classList = Arrays.asList(partitionClasses);
		Map<Class, List<T>> result = Maps.newMap();
		for (T item : input) {
			int index = findClass(classList, item);
			if (index == -1)
				throw new IllegalArgumentException("Item of " + item.getClass() + " found, which is not in " + classList);
			Class key = classList.get(index);
			Maps.addToList(result, key, item);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> int findClass(List<Class> classList, T item) {
		Class<? extends Object> itemClass = item.getClass();
		int i = 0;
		for (Class clazz : classList) {
			if (clazz.isAssignableFrom(itemClass))
				return i;
			i++;
		}
		return -1;
	}

	public static <T> List<T> newList() {
		return new ArrayList<T>();
	}

	public static <T> List<T> newList(int size) {
		return new ArrayList<T>(size);
	}

	public static <T extends Comparable<T>> List<T> sort(Iterable<T> from) {
		List<T> result = makeListFor(from);
		for (T t : from)
			result.add(t);
		Collections.sort(result);
		return result;

	}

	public static <T> List<T> append(List<T> initial, T... more) {
		List<T> result = new ArrayList<T>(initial.size() + more.length);
		result.addAll(initial);
		result.addAll(Arrays.asList(more));
		return result;
	}

	public static <T> List<T> addAtStart(List<T> initial, T... more) {
		List<T> result = new ArrayList<T>(initial.size() + more.length);
		result.addAll(Arrays.asList(more));
		result.addAll(initial);
		return result;
	}

}
