package org.fastorm.stats;

import org.fastorm.api.IJob;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.functions.IFunction1;

public class ChampionChallengerStatsExecutionVisitor extends ChampionChallengerStatsExecutionVisitorRaw {

	public static IProfilable<IJob, IJob> toTime(final ICallback<IJob> rawToTime, final IFunction1<IJob, IJob> fn) {
		return new IProfilable<IJob, IJob>() {

			@Override
			public IJob start(IJob situation) throws Exception {
				return fn.apply(situation);
			}

			@Override
			public void job(IJob situation, IJob context) throws Exception {
				rawToTime.process(fn.apply(context));
			}

			@Override
			public void finish(IJob situation, IJob context) {
			}
		};
	}

	ChampionChallengerStatsExecutionVisitor(ICallback<IJob> toTime, String championName, IFunction1<IJob, IJob> championFn, String challengerName,
			IFunction1<IJob, IJob> challengerFn) {
		super(championName, toTime(toTime, championFn), challengerName, toTime(toTime, challengerFn));
	}

}