package org.fastorm.utilities.strings;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.fastorm.utilities.constants.UtilityConstants;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.tests.Tests;

public class AbstractSimpleStringTest extends TestCase {

	public void testSimpleStringsAreLengthZeroWhenCreated() {
		assertEquals(0, new SimpleString(0).length());
		assertEquals(0, new SimpleString(20).length());
	}

	public void testSimpleStringsCanBeCreatedFromStrings() {
		SimpleString simpleString = new SimpleString("abc");
		assertEquals(3, simpleString.length());
		assertEquals('a', simpleString.byteAt(0));
		assertEquals('b', simpleString.byteAt(1));
		assertEquals('c', simpleString.byteAt(2));
	}

	public void testCanSetLengthAndThisChangesEffectiveValue() {
		SimpleString simpleString = new SimpleString("abc");
		assertTrue(ISimpleString.Utils.equivalent(simpleString, "abc"));
		simpleString.setLength(0);
		assertTrue(ISimpleString.Utils.equivalent(simpleString, ""));
		simpleString.setLength(1);
		assertTrue(ISimpleString.Utils.equivalent(simpleString, "a"));
		simpleString.setLength(2);
		assertTrue(ISimpleString.Utils.equivalent(simpleString, "ab"));
		simpleString.setLength(3);
		assertTrue(ISimpleString.Utils.equivalent(simpleString, "abc"));
	}

	public void testCanSetBytes() throws UnsupportedEncodingException {
		SimpleString simpleString = new SimpleString("abc");
		assertTrue(ISimpleString.Utils.equivalent(simpleString, "abc"));
		simpleString.setBytes("def".getBytes(UtilityConstants.defaultStringCoding));
		assertTrue(ISimpleString.Utils.equivalent(simpleString, "def"));
	}

	public void testSetBytesThrowsExceptionIfArrayTooBig() {
		final SimpleString simpleString = new SimpleString("abc");
		Tests.assertThrows(IllegalArgumentException.class, new Runnable() {
			public void run() {
				try {
					simpleString.setBytes("defg".getBytes(UtilityConstants.defaultStringCoding));
				} catch (UnsupportedEncodingException e) {
					throw WrappedException.wrap(e);
				}
			}
		});
	}

	public void testSetLengthThrowsIllegalArgumentExceptionIfNegativeOrGreaterThanByteArrayLength() {
		checkSetLengthThrowsIllegalArgumentExceptionIfNegativeOrGreaterThanByteArrayLength(-1);
		checkSetLengthThrowsIllegalArgumentExceptionIfNegativeOrGreaterThanByteArrayLength(4);
	}

	private void checkSetLengthThrowsIllegalArgumentExceptionIfNegativeOrGreaterThanByteArrayLength(final int length) {
		final SimpleString simpleString = new SimpleString("abc");
		Tests.assertThrows(IllegalArgumentException.class, new Runnable() {
			public void run() {
				simpleString.setLength(length);
			}
		});

	}

	public void testSimpleStringsThrowIndexOutOfBoundsExceptions() {
		checkSimpleStringsThrowIndexOutOfBoundsExceptions(-1);
		checkSimpleStringsThrowIndexOutOfBoundsExceptions(4);
	}

	private void checkSimpleStringsThrowIndexOutOfBoundsExceptions(final int value) {
		final SimpleString simpleString = new SimpleString("abc");
		Tests.assertThrows(IndexOutOfBoundsException.class, new Runnable() {
			public void run() {
				simpleString.byteAt(value);
			}
		});
	}

	public void testEquivalance() {
		SimpleString abc = new SimpleString("abc");
		assertTrue(SimpleString.Utils.equivalent("abc", abc));
		assertFalse(SimpleString.Utils.equivalent("abd", abc));
		assertFalse(SimpleString.Utils.equivalent("ab", abc));
		assertFalse(SimpleString.Utils.equivalent("abcd", abc));
		assertFalse(SimpleString.Utils.equivalent(null, abc));
		assertFalse(SimpleString.Utils.equivalent("abc", null));
	}

}
