package org.fastorm.stats;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrm;
import org.fastorm.utilities.Maps;

public class StatsExecutionVisitor implements IFastOrmExecutorVisitor {
	private final Spec initialSpec;
	private final Map<Integer, StatsMap> statsMap = Maps.newMap();
	private List<Integer> databaseSizes;
	private final ICallback<IFastOrm> toTime;

	StatsExecutionVisitor(Spec initialSpec, ICallback<IFastOrm> toTime) {
		this.initialSpec = initialSpec;
		this.toTime = toTime;
	}

	@Override
	public void atStart(Spec spec, IFastOrm initial, ExerciseNumbers numbers) {
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
	public void innerRun(int outerRun, int databaseSize, Spec spec, IFastOrm fastOrm, int innerRun) throws Exception {
		long startTime = System.currentTimeMillis();
		Maps.findOrCreate(statsMap, databaseSize, new Callable<StatsMap>() {
			@Override
			public StatsMap call() throws Exception {
				return new StatsMap();
			}
		});
		toTime.process(fastOrm);
		long duration = System.currentTimeMillis() - startTime;
		statsMap.get(databaseSize).add(fastOrm, databaseSize, duration);
	}

	@Override
	public void endTest(int outerRun, int databaseSize, Spec spec, IFastOrm fastOrm) {
		Stats stats = statsMap.get(databaseSize).get(fastOrm);
		System.out.println(spec.stringFor(fastOrm) + String.format(" %10d %10.2f", stats.averageDuration(), stats.averageDuration() * 1.0 / databaseSize));
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
			for (IFastOrm fastOrm : spec) {
				Stats stats = map.get(fastOrm);
				System.out.println(spec.stringFor(fastOrm) + String.format(" %10d %10.2f", stats.averageDuration(), stats.averageDuration() * 1.0 / databaseSize));
			}
		}
	}

	public Map<Integer, StatsMap> getStatsMap() {
		return statsMap;
	}
}