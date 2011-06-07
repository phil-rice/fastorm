package org.fastorm.utilities.strings;

import java.util.List;

import org.fastorm.utilities.aggregators.IAggregator;

public class Strings {
	public static <T> String join(Iterable<T> from, String separator) {
		StringBuilder builder = new StringBuilder();
		boolean addSeparator = false;
		for (T f : from) {
			if (addSeparator)
				builder.append(separator);
			builder.append(f);
			addSeparator = true;
		}
		return builder.toString();
	}

	public static String join(List<String> from, List<Integer> indicies, String separator) {
		StringBuilder builder = new StringBuilder();
		boolean addSeparator = false;
		for (int i = 0; i < indicies.size(); i++) {
			String f = from.get(indicies.get(i));
			if (addSeparator)
				builder.append(separator);
			builder.append(f);
			addSeparator = true;
		}
		return builder.toString();
	}

	public static String oneLine(String string) {
		return string.replaceAll("\n", " ");
	}

	public static IAggregator<String, String> strJoin(final String separator) {
		return Strings.<String> join(separator);
	}

	public static <T> IAggregator<T, String> join(final String separator) {
		return new IAggregator<T, String>() {
			private final StringBuilder builder = new StringBuilder();
			private boolean addSeparator;

			@Override
			public void add(T t) {
				if (addSeparator)
					builder.append(separator);
				addSeparator = true;
				builder.append(t);
			}

			@Override
			public String result() {
				return builder.toString();
			}
		};

	}

}
