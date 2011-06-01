package org.fastorm.stats;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fastorm.api.IFastOrm;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.Maps;

public class StatsMap implements ISimpleMap<IFastOrm, Stats> {

	static class Utils {
		public static final Callable<StatsMap> newStatsMap = new Callable<StatsMap>() {
			@Override
			public StatsMap call() throws Exception {
				return new StatsMap();
			}
		};
	}

	private final Map<IFastOrm, Stats> map = Maps.newMap();

	public void add(IFastOrm fastOrm, int databaseSize, long duration) {
		Maps.findOrCreate(map, fastOrm, new Callable<Stats>() {
			@Override
			public Stats call() throws Exception {
				return new Stats();
			}
		}).add(duration);
	}

	@Override
	public Stats get(IFastOrm key) {
		return map.get(key);
	}

	@Override
	public List<IFastOrm> keys() {
		return Iterables.list(map.keySet());
	}

}
