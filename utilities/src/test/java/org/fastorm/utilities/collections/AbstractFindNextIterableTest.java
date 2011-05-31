package org.fastorm.utilities.collections;

import java.util.Arrays;
import java.util.Iterator;

import org.fastorm.utilities.collections.AbstractFindNextIterable;
import org.fastorm.utilities.collections.Iterables;

import junit.framework.TestCase;

public class AbstractFindNextIterableTest extends TestCase {

	public void testFindNextIterator() {
		checkIterable();
	}

	private void checkIterable(final Integer... ints) {
		Iterable<Integer> iterable = new AbstractFindNextIterable<Integer, Iterator<Integer>>() {
			@Override
			protected Integer findNext(Iterator<Integer> context) {
				return context.hasNext() ? context.next() : null;
			}

			@Override
			protected Iterator<Integer> reset() {
				return Arrays.asList(ints).iterator();
			}

		};
		assertEquals(Arrays.asList(ints), Iterables.list(iterable));
		assertEquals(Arrays.asList(ints), Iterables.list(iterable));
		assertEquals(Arrays.asList(ints), Iterables.list(iterable));
		assertEquals(Arrays.asList(ints), Iterables.list(iterable));
	}
}
