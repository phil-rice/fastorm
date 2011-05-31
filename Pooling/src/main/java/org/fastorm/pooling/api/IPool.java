package org.fastorm.pooling.api;

import org.fastorm.pooling.impl.Pool;
import org.fastorm.pooling.impl.StringPool;
import org.fastorm.pooling.impl.ThreadSaferPool;
import org.fastorm.pooling.impl.ThreadUnsafePool;
import org.fastorm.utilities.strings.ISimpleString;
import org.fastorm.utilities.strings.SimpleString;

/** Each pool manages objects of type T */
public interface IPool<T> extends IPoolThin<T> {

	void prepopulate();

	T newObject();

	static class Utils {

		public static <T> IPool<T> pool(PoolOptions poolOptions, IObjectDefinition<T> defn) {
			IPool<T> result = new Pool<T>(findThinInterface(poolOptions, defn));
			result.prepopulate();
			return result;
		}

		private static <T> IPoolThin<T> findThinInterface(PoolOptions poolOptions, IObjectDefinition<T> defn) {
			return poolOptions.tryToBeThreadSafe ? new ThreadSaferPool<T>(poolOptions, defn) : new ThreadUnsafePool<T>(poolOptions, defn);
		}

		public static IStringPool makeStringPool(PoolOptions poolOptions, final int maxStringLength) {
			IObjectDefinition<ISimpleString> defn = new IObjectDefinition<ISimpleString>() {
				@Override
				public Class<ISimpleString> objectClass() {
					return ISimpleString.class;
				}

				@Override
				public ISimpleString createBlank() {
					return new SimpleString(maxStringLength);
				}

				@Override
				public void clean(ISimpleString oldObject) {
					((SimpleString) oldObject).setLength(0);
				}
			};
			IStringPool result = new StringPool(findThinInterface(poolOptions, defn));
			result.prepopulate();
			return result;
		}

	}

}
