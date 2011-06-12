package org.fastorm.api;

import javax.sql.DataSource;

import org.fastorm.api.impl.Job;
import org.fastorm.constants.FastOrmJobOptimisations;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.mutate.IMutator;
import org.fastorm.reader.IEntityReader;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.ISimpleMap;

public interface IJob extends IJobConfigurator, IJobDetails {

	IEntityReader<ISimpleMap<String, Object>> makeReader();

	<T> IMutator<T> makeMutator();

	IFastOrmContainer getContainer();

	static class Utils {
		public static <T> IJob mySqlSingleThreaded(IEntityDefn entityDefn, DataSource dataSource) {
			return new Job(entityDefn, dataSource);

		}

		public static IFunction1<IJob, Integer> getBatchSize() {
			return new IFunction1<IJob, Integer>() {

				@Override
				public Integer apply(IJob from) throws Exception {
					return from.getBatchSize();
				}
			};
		}

		public static IFunction1<IJob, String> getMemoryManagerSimpleClassname() {
			return new IFunction1<IJob, String>() {

				@Override
				public String apply(IJob from) throws Exception {
					return from.getContainer().getMemoryManager().getClass().getSimpleName();
				}
			};
		}

		public static IFunction1<IJob, Boolean> getUseTemporaryTables() {
			return new IFunction1<IJob, Boolean>() {
				@Override
				public Boolean apply(IJob from) throws Exception {
					return from.getOptimisations().optimise(FastOrmJobOptimisations.useTemporaryTables);
				}
			};
		}

	}

}
