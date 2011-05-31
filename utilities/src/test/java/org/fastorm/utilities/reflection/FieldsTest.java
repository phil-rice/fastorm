package org.fastorm.utilities.reflection;

import java.util.Arrays;

import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.reflection.Fields;

import junit.framework.TestCase;

public class FieldsTest extends TestCase {

	public int intField;
	public static int staticIntField = 0;
	public static final int staticFinalIntField = 0;
	@SuppressWarnings("unused")
	private static final int privateStaticFinalIntField = 0;

	public void testFields() {
		assertEquals(Arrays.asList("intField", "staticIntField", "staticFinalIntField"), Iterables.list(Fields.names(Fields.publicFields(getClass()))));
		assertEquals(Arrays.asList("staticFinalIntField"), Iterables.list(Fields.names(Fields.constants(getClass()))));
	}
}
