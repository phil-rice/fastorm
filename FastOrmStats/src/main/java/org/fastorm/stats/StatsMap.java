package org.fastorm.stats;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fastorm.api.IJob;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.Maps;

public class StatsMap implements ISimpleMap<IJob, Stats> {

	static class Utils {
		public static final Callable<StatsMap> newStatsMap = new Callable<StatsMap>() {
			@Override
			public StatsMap call() throws Exception {
				return new StatsMap();
			}
		};
	}

	private final Map<IJob, Stats> map = Maps.newMap();

	public void add(IJob job, int databaseSize, long duration) {
		Maps.findOrCreate(map, job, new Callable<Stats>() {
			@Override
			public Stats call() throws Exception {
				return new Stats();
			}
		}).add(duration);
	}

	@Override
	public Stats get(IJob key) {
		return map.get(key);
	}

	@Override
	public List<IJob> keys() {
		return Iterables.list(map.keySet());
	}

}
