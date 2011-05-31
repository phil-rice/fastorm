package org.fastorm.stats;

import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrm;
import org.fastorm.utilities.IFunction1;

public class ChampionChallengerStatsExecutionVisitor extends ChampionChallengerStatsExecutionVisitorRaw {

	public static IProfilable<IFastOrm, IFastOrm> toTime(final ICallback<IFastOrm> rawToTime, final IFunction1<IFastOrm, IFastOrm> fn) {
		return new IProfilable<IFastOrm, IFastOrm>() {

			@Override
			public IFastOrm start(IFastOrm situation) throws Exception {
				return fn.apply(situation);
			}

			@Override
			public void job(IFastOrm situation, IFastOrm context) throws Exception {
				rawToTime.process(fn.apply(context));
			}

			@Override
			public void finish(IFastOrm situation, IFastOrm context) {
			}
		};
	}

	ChampionChallengerStatsExecutionVisitor(ICallback<IFastOrm> toTime, String championName, IFunction1<IFastOrm, IFastOrm> championFn, String challengerName,
			IFunction1<IFastOrm, IFastOrm> challengerFn) {
		super(championName, toTime(toTime, championFn), challengerName, toTime(toTime, challengerFn));
	}

}