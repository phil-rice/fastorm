package org.fastorm.utilities;

import org.fastorm.utilities.aggregators.IAggregator;

public class Strings {
	public static String join(Iterable<String> from, String separator) {
		StringBuilder builder = new StringBuilder();
		boolean addSeparator = false;
		for (String f : from) {
			if (addSeparator)
				builder.append(separator);
			builder.append(f);
			addSeparator = true;
		}
		return builder.toString();
	}

	public static String oneLine(String string){
		return string.replaceAll("\n", " ");
	}
	public static IAggregator<String, String> strJoin(final String separator) {
		return Strings.<String> join(separator);
	}

	public static <T> IAggregator<T, String> join(final String separator) {
		return new IAggregator<T, String>() {
			private final StringBuilder builder = new StringBuilder();
			private boolean addSeparator;

			public void add(T t) {
				if (addSeparator)
					builder.append(separator);
				addSeparator = true;
				builder.append(t);
			}

			public String result() {
				return builder.toString();
			}
		};

	}

}
