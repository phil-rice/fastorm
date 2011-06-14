package org.fastorm.stats;

import org.fastorm.api.IJob;
import org.fastorm.temp.ITempTableMakerFactory;
import org.fastorm.utilities.functions.IFunction1;

public class CompareStoredProcedures {

	public static void main(String[] args) throws Exception {
		final IJob initial = FastOrmExerciser.Utils.makeInitial().withCreateAndDropAtStart(false);
		new FastOrmExerciser(new ExerciseNumbers(), //
				FastOrmExerciser.Utils.specWithBatch1_10_100_1000(initial), //
				new ChampionChallengerStatsExecutionVisitor(//
						FastOrmExerciser.Utils.getAllItemsCallback,//
						"Mysql.st", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withTempTableMaker(ITempTableMakerFactory.Utils.withoutStoredProcedures());
							}
						}, //
						"StoredProcs", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withTempTableMaker(ITempTableMakerFactory.Utils.withStoredProcedures());
							}
						}));

	}
}
