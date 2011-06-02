package org.fastorm.utilities.collections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

public class Sets {

	public static <T> Set<T> makeSet(T... ts) {
		return new HashSet<T>(Arrays.asList(ts));
	}

	public static <T> Set<T> set(Iterable<T> iterable) {
		Set<T> result = new HashSet<T>();
		for (T t : iterable)
			result.add(t);
		return result;
	}

	public static <T1, T2> void assertMatches(Iterable<T1> left, Iterable<T2> right) {
		Assert.assertEquals(Sets.set(left), Sets.set(right));

	}
}
