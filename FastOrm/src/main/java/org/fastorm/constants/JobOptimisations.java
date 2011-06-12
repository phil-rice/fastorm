package org.fastorm.constants;

import java.util.IdentityHashMap;
import java.util.Map;

import org.fastorm.api.IJobOptimisations;
import org.fastorm.utilities.maps.Maps;

public class JobOptimisations implements IJobOptimisations {

	private final Map<String, Boolean> map;

	public JobOptimisations() {
		this(Maps.<String, Boolean> newMap(IdentityHashMap.class));
	}

	public JobOptimisations(Map<String, Boolean> map) {
		this.map = map;
	}

	@Override
	public boolean indexPrimaryTables() {
		return optimise(FastOrmJobOptimisations.indexPrimaryTables);
	}

	@Override
	public boolean indexSecondaryTables() {
		return optimise(FastOrmJobOptimisations.indexSecondaryTables);
	}

	@Override
	public boolean useTemporaryTables() {
		return optimise(FastOrmJobOptimisations.useTemporaryTables);
	}

	@Override
	public boolean optimiseLeafAccess() {
		return optimise(FastOrmJobOptimisations.optimiseLeafAccess);
	}

	@Override
	public boolean optimise(String optimisationName) {
		Boolean result = map.get(optimisationName);
		return result == null ? false : result.booleanValue();
	}

	@Override
	public IJobOptimisations withOptimisation(String optimisationName, Boolean value) {
		return new JobOptimisations(Maps.with(map, optimisationName, value));
	}

}
