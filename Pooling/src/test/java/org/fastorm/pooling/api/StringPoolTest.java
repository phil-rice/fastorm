package org.fastorm.pooling.api;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.fastorm.utilities.constants.UtilityConstants;
import org.fastorm.utilities.strings.ISimpleString;
import org.fastorm.utilities.strings.SimpleString;

public class StringPoolTest extends TestCase {
	private IStringPool pool;
	private ISimpleString string0;

	public void testNewStringWithString() throws UnsupportedEncodingException {
		checkStringSameAndEquals(pool.newString("abc"), string0, "abc");
	}

	public void testNewStringWithBytes() throws UnsupportedEncodingException {
		byte[] bytes = "abcdef".getBytes(UtilityConstants.defaultStringCoding);
		checkStringSameAndEquals(pool.newString(bytes, 1, 3), string0, "bcd");
	}

	public void testNewStringWithSimpleString() throws UnsupportedEncodingException {
		checkStringSameAndEquals(pool.newString(new SimpleString("abcdef"), 1, 2), string0, "bc");
	}

	private void checkStringSameAndEquals(ISimpleString newString, ISimpleString oldString, String expected) {
		assertSame(oldString, newString);
		String prefix = "NewString[" + newString + "] expected [" + expected + "]";
		assertTrue(prefix, ISimpleString.Utils.equivalent(newString, expected));
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		pool = IPool.Utils.makeStringPool(new PoolOptions(), 5);
		string0 = pool.newObject();
		pool.dispose();

	}
}
