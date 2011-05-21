package org.fastorm.api;

import javax.sql.DataSource;

import org.fastorm.api.impl.FastOrm;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.mutate.IMutate;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.ISimpleMap;

public interface IFastOrm extends IFastOrmFactoryConfigurator {

	IEntityReader<ISimpleMap<String, Object>> makeReader();

	<T> IMutate<T> makeMutator();

	int getMaxForOneThread();

	int getBatchSize();

	IEntityDefn getEntityDefn();

	IFastOrmContainer getContainer();

	static class Utils {
		public static <T> IFastOrm mySqlSingleThreaded(IEntityDefn entityDefn, DataSource dataSource) {
			return new FastOrm(entityDefn, dataSource);

		}

		public static IFunction1<IFastOrm, Integer> getBatchSize() {
			return new IFunction1<IFastOrm, Integer>() {

				@Override
				public Integer apply(IFastOrm from) throws Exception {
					return from.getBatchSize();
				}
			};
		}

		public static IFunction1<IFastOrm, String> getMemoryManagerSimpleClassname() {
			return new IFunction1<IFastOrm, String>() {

				@Override
				public String apply(IFastOrm from) throws Exception {
					return from.getContainer().getMemoryManager().getClass().getSimpleName();
				}
			};
		}

		public static IFunction1<IFastOrm, Boolean> getUseTemporaryTables() {
			return new IFunction1<IFastOrm, Boolean>() {

				@Override
				public Boolean apply(IFastOrm from) throws Exception {
					return from.getContainer().getOptions().useTemporaryTables;
				}
			};
		}
	}

}
