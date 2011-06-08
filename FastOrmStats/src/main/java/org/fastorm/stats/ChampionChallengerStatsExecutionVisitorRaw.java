package org.fastorm.stats;

import java.util.List;
import java.util.Map;

import org.fastorm.api.IJob;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.maps.Maps;

public class ChampionChallengerStatsExecutionVisitorRaw implements IFastOrmExecutorVisitor {

	private List<Integer> databaseSizes;
	private final Map<Integer, StatsMap> championStatsMap = Maps.newMap();
	private final Map<Integer, StatsMap> challengerStatsMap = Maps.newMap();
	private Spec spec;
	private final String championName;
	private final String challengerName;
	private final IProfilable<IJob, ?> championCallback;
	private final IProfilable<IJob, ?> challengerCallback;

	public ChampionChallengerStatsExecutionVisitorRaw(String championName, IProfilable<IJob, ?> championCallback, String challengerName, IProfilable<IJob, ?> challengerCallback) {
		this.championName = championName;
		this.championCallback = championCallback;
		this.challengerName = challengerName;
		this.challengerCallback = challengerCallback;
	}

	@Override
	public void atStart(Spec spec, IJob initial, ExerciseNumbers numbers) {
		this.spec = spec;
		this.databaseSizes = numbers.databaseSizes;
		System.out.println("Initial: " + initial);
	}

	@Override
	public void startOuterRun(int outerRun) {
	}

	@Override
	public void startDatabase(int outerRun, int databaseSize, Spec spec) {
		printTitle(spec);
	}

	@Override
	public void startTest(int outerRun, Spec spec) {
	}

	@Override
	public void innerRun(int outerRun, int databaseSize, Spec spec, IJob job, int innerRun) throws Exception {
		time(databaseSize, job, challengerStatsMap, challengerCallback);
		time(databaseSize, job, championStatsMap, championCallback);
	}

	private <Context> void time(int databaseSize, IJob job, Map<Integer, StatsMap> statsMap, IProfilable<IJob, Context> profilable) throws Exception {
		try {
			Maps.findOrCreate(statsMap, databaseSize, StatsMap.Utils.newStatsMap);
			Context context = profilable.start(job);
			long startTime = System.currentTimeMillis();
			profilable.job(job, context);
			long duration = System.currentTimeMillis() - startTime;
			profilable.finish(job, context);
			statsMap.get(databaseSize).add(job, databaseSize, duration);
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public void endTest(int outerRun, int databaseSize, Spec spec, IJob job) {
		printStats(databaseSize, spec, job);
	}

	@Override
	public void endDatabase(int outerRun, int databaseSize) {
	}

	@Override
	public void endOuterRun(int outerRun) {
		System.out.println("End of run " + outerRun);
		System.out.println(spec.withDatabaseSize(0).title() + "   Duration    PerItem");
		for (int databaseSize : databaseSizes) {
			Spec thisSpec = spec.withDatabaseSize(databaseSize);
			printTitle(thisSpec);
			for (IJob job : thisSpec) {
				printStats(databaseSize, thisSpec, job);
			}
		}
	}

	private void printTitle(Spec spec) {
		System.out.println(spec.title() + String.format(" %-21s", championName) + String.format(" %-21s", challengerName) + String.format(" %10s", "Better"));
	}

	private void printStats(int databaseSize, Spec spec, IJob job) {
		Stats championStats = championStatsMap.get(databaseSize).get(job);
		Stats challangerStats = challengerStatsMap.get(databaseSize).get(job);
		long championDuration = championStats.averageDuration();
		long challengerDuration = challangerStats.averageDuration();
		String result = spec.stringFor(job) + String.format(" %10d %10.2f %10d %10.2f %10.2f", //
				championDuration, championDuration * 1.0 / databaseSize,//
				challengerDuration, challengerDuration * 1.0 / databaseSize, championDuration * 1.0 / challengerDuration);
		System.out.println(result);
	}

}