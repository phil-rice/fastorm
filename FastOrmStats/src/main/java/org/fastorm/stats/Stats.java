package org.fastorm.stats;

import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;

class Stats {

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