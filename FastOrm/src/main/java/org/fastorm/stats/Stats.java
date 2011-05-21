package org.fastorm.stats;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.fastorm.utilities.Maps;

class Stats {

	static void add(Map<Object, Stats> statsMap, long duration, Object key) {
		Stats stats = Maps.findOrCreate(statsMap, key, new Callable<Stats>() {
			@Override
			public Stats call() throws Exception {
				return new Stats();
			}
		});
		stats.add(duration);
	}

	StandardDeviation deviation = new StandardDeviation();
	private long totalDuration;
	int count;

	public void add(long duration) {
		count++;
		totalDuration += duration;
		deviation.increment(duration);
	}

	long averageDuration() {
		return totalDuration / count;
	}

	double standardDeviation() {
		return deviation.getResult();
	}

}