package org.fastorm.utilities.callbacks;

public interface ICallback<T> {
	void process(T t) throws Exception;

	static class Utils {
		public static final <T> ICallback<T> noCallback() {
			return new NoCallback<T>();
		};

		public static ICallback<Integer> count = new ICallback<Integer>() {
			@Override
			public void process(Integer count) throws Exception {
				if (count != 0 && count % 50 == 0)
					System.out.println(count);
				count++;
				System.out.print(".");
			}
		};
	}
}
