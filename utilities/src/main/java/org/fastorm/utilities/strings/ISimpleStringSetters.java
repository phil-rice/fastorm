package org.fastorm.utilities.strings;

import org.fastorm.utilities.strings.ISimpleStringSetters;


/** This is a very simple string that uses UTF-8 character encoding. It is small and poolable */
public interface ISimpleStringSetters {

	int length();

	byte byteAt(int offset);

	static class Utils {

		public static boolean equivalent(ISimpleStringSetters simpleString, String string) {
			if (simpleString == null || string == null)
				return simpleString == null && string == null;
			int length = simpleString.length();
			if (length != string.length())
				return false;
			for (int i = 0; i < length; i++)
				if (simpleString.byteAt(i) != string.charAt(i))
					return false;
			return true;
		}

		public static final ISimpleStringSetters empty = new ISimpleStringSetters() {

			@Override
			public int length() {
				return 0;
			}

			@Override
			public byte byteAt(int offset) {
				throw new IndexOutOfBoundsException(Integer.toString(offset));
			}
		};

		public static boolean equivalent(String string, SimpleString simpleString) {
			if (string == null || simpleString == null)
				return string == null && simpleString == null;
			if (string.length() != simpleString.length())
				return false;
			for (int i = 0; i < string.length(); i++)
				if ((byte) string.charAt(i) != simpleString.byteAt(i))
					return false;
			return true;
		}

	}

}
