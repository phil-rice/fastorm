package org.fastorm.stats;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fastorm.api.IJob;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.maps.Maps;

public class StatsExecutionVisitor implements IFastOrmExecutorVisitor {
	private final Spec initialSpec;
	private final Map<Integer, StatsMap> statsMap = Maps.newMap();
	private List<Integer> databaseSizes;
	private final ICallback<IJob> toTime;

	StatsExecutionVisitor(Spec initialSpec, ICallback<IJob> toTime) {
		this.initialSpec = initialSpec;
		this.toTime = toTime;
	}

	@Override
	public void atStart(Spec spec, IJob initial, ExerciseNumbers numbers) {
		this.databaseSizes = numbers.databaseSizes;
	}

	@Override
	public void startOuterRun(int outerRun) {
	}

	@Override
	public void startDatabase(int outerRun, int databaseSize, Spec spec) {
		System.out.println(spec.title() + "   Duration    PerItem");
	}

	@Override
	public void startTest(int outerRun, Spec spec) {
	}

	@Override
	public void innerRun(int outerRun, int databaseSize, Spec spec, IJob job, int innerRun) throws Exception {
		long startTime = System.currentTimeMillis();
		Maps.findOrCreate(statsMap, databaseSize, new Callable<StatsMap>() {
			@Override
			public StatsMap call() throws Exception {
				return new StatsMap();
			}
		});
		toTime.process(job);
		long duration = System.currentTimeMillis() - startTime;
		statsMap.get(databaseSize).add(job, databaseSize, duration);
	}

	@Override
	public void endTest(int outerRun, int databaseSize, Spec spec, IJob job) {
		Stats stats = statsMap.get(databaseSize).get(job);
		System.out.println(spec.stringFor(job) + String.format(" %10d %10.2f", stats.averageDuration(), stats.averageDuration() * 1.0 / databaseSize));
	}

	@Override
	public void endDatabase(int outerRun, int databaseSize) {
	}

	@Override
	public void endOuterRun(int outerRun) {
		System.out.println("End of run " + outerRun);
		System.out.println(initialSpec.withDatabaseSize(0).title() + "   Duration    PerItem");
		for (int databaseSize : databaseSizes) {
			Spec spec = initialSpec.withDatabaseSize(databaseSize);
			StatsMap map = statsMap.get(databaseSize);
			for (IJob job : spec) {
				Stats stats = map.get(job);
				System.out.println(spec.stringFor(job) + String.format(" %10d %10.2f", stats.averageDuration(), stats.averageDuration() * 1.0 / databaseSize));
			}
		}
	}

	public Map<Integer, StatsMap> getStatsMap() {
		return statsMap;
	}
}