package org.fastorm.api;

import java.util.IdentityHashMap;

import org.fastorm.constants.FastOrmJobOptimisations;
import org.fastorm.constants.JobOptimisations;
import org.fastorm.utilities.maps.Maps;

public interface IJobOptimisations {
	boolean optimise(String optimisationName);

	boolean indexPrimaryTables();

	boolean indexSecondaryTables();

	boolean useTemporaryTables();

	boolean optimiseLeafAccess();

	IJobOptimisations withOptimisation(String optimisationName, Boolean value);

	static class Utils {

		public static IJobOptimisations usualBest() {
			return new JobOptimisations();
		}

		public static IJobOptimisations withNoOptimisation() {
			return new JobOptimisations(Maps.<String, Boolean> newMap(IdentityHashMap.class));
		}

		public static IJobOptimisations withTempTables(boolean value) {
			return new JobOptimisations().withOptimisation(FastOrmJobOptimisations.useTemporaryTables, value);
		}
	}

}
