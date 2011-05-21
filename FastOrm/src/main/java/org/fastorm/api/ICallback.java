package org.fastorm.api;

public interface ICallback<T> {
	void process(T t) throws Exception;

	static class Utils {
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
