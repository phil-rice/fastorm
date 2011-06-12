package org.fastorm.stats;

import org.fastorm.api.IJob;
import org.fastorm.api.IJobOptimisations;
import org.fastorm.constants.FastOrmJobOptimisations;
import org.fastorm.utilities.functions.IFunction1;

public class CompareOptimiseLeaf {

	public static void main(String[] args) throws Exception {
		final IJob initial = FastOrmExerciser.Utils.makeInitial().withCreateAndDropAtStart(false);
		final IJobOptimisations optimisations = initial.getOptimisations();
		new FastOrmExerciser(new ExerciseNumbers(), //
				FastOrmExerciser.Utils.specWithBatch1_10_100_1000(initial), //
				new ChampionChallengerStatsExecutionVisitor(//
						FastOrmExerciser.Utils.getAllItemsCallback,//
						"Treat_Same", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withOptimisations(optimisations.withOptimisation(FastOrmJobOptimisations.optimiseLeafAccess, false));
							}
						}, //
						"Optimise_Leaf", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withOptimisations(optimisations.withOptimisation(FastOrmJobOptimisations.optimiseLeafAccess, true));
							}
						}));

	}
}
