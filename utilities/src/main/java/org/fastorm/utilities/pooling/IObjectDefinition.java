package org.fastorm.utilities.pooling;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.List;

import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.maps.ArraySimpleMap;
import org.fastorm.utilities.strings.ByteArraySimpleString;
import org.fastorm.utilities.strings.ByteBufferSimpleString;
import org.fastorm.utilities.strings.ISimpleStringWithSetters;

public interface IObjectDefinition<T> {

	Class<T> objectClass();

	T createBlank();

	void clean(T oldObject);

	static class Utils {
		public static IObjectDefinition<ISimpleStringWithSetters> arraySimpleStringDefn(final int maxStringLength) {
			return new IObjectDefinition<ISimpleStringWithSetters>() {
				@Override
				public Class<ISimpleStringWithSetters> objectClass() {
					return ISimpleStringWithSetters.class;
				}

				@Override
				public ISimpleStringWithSetters createBlank() {
					return new ByteArraySimpleString(new byte[maxStringLength]);
				}

				@Override
				public void clean(ISimpleStringWithSetters oldObject) {
					oldObject.setFromString("");
				}
			};
		}

		public static IObjectDefinition<ISimpleStringWithSetters> bufferSimpleStringDefn(final int maxStringLength) {
			return new IObjectDefinition<ISimpleStringWithSetters>() {
				@Override
				public Class<ISimpleStringWithSetters> objectClass() {
					return ISimpleStringWithSetters.class;
				}

				@Override
				public ISimpleStringWithSetters createBlank() {
					return new ByteBufferSimpleString(ByteBuffer.allocate(maxStringLength));
				}

				@Override
				public void clean(ISimpleStringWithSetters oldObject) {
					oldObject.setFromString("");
				}
			};
		}

		@SuppressWarnings("unchecked")
		public static <T> IObjectDefinition<T[]> arrayDefn(final Class<T> clazz, final int arrayLength) {
			final Class<T[]> arrayClass = (Class<T[]>) Array.newInstance(clazz, 0).getClass();
			return new IObjectDefinition<T[]>() {
				@Override
				public Class<T[]> objectClass() {
					return arrayClass;
				}

				@Override
				public T[] createBlank() {
					return (T[]) Array.newInstance(clazz, arrayLength);
				}

				@Override
				public void clean(T[] oldObject) {
					for (int i = 0; i < oldObject.length; i++)
						oldObject[i] = null;
				}
			};
		}

		@SuppressWarnings("unchecked")
		public static <K, V> IObjectDefinition<ArraySimpleMap<K, V>> arraySimpleMapDefn(final List<K> keys, final Class<V> valueClass) {
			return arraySimpleMapDefn(keys, ArraySimpleMap.class, valueClass);
		}

		public static <K, V, T extends ArraySimpleMap<K, V>> IObjectDefinition<T> arraySimpleMapDefn(final List<K> keys, final Class<T> resultClass, final Class<V> valueClass) {
			return new IObjectDefinition<T>() {
				@Override
				public Class<T> objectClass() {
					return resultClass;
				}

				@Override
				public T createBlank() {
					try {
						Constructor<T> constructor = resultClass.getConstructor(new Class[] { List.class, Class.class });
						T map = constructor.newInstance(keys, valueClass);
						return map;
					} catch (Exception e) {
						throw WrappedException.wrap(e);
					}
				}

				@Override
				public void clean(T oldObject) {
					V[] values = oldObject.getValues();
					for (int i = 0; i < values.length; i++)
						values[i] = null;
				}
			};
		}
	}
}
