package org.fastorm.api;

import javax.sql.DataSource;

import org.fastorm.api.impl.FastOrm;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.mutate.IMutate;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.ISimpleMap;

public interface IFastOrm extends IFastOrmFactoryConfigurator {

	IEntityReader<ISimpleMap<String, Object>> makeReader();

	<T> IMutate<T> makeMutator();

	int getMaxForOneThread();

	int getDataSetSize();

	IEntityDefn getEntityDefn();

	IFastOrmContainer getContainer();

	static class Utils {
		public static <T> IFastOrm mySqlSingleThreaded(IEntityDefn entityDefn, DataSource dataSource) {
			return new FastOrm(entityDefn, dataSource);

		}
	}

}
