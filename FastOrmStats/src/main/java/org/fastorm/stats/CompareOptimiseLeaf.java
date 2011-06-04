package org.fastorm.stats;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.IFastOrm;
import org.fastorm.utilities.functions.IFunction1;

public class CompareOptimiseLeaf {

	public static void main(String[] args) throws Exception {
		FastOrmOptions options = new FastOrmOptions().withCreateAndDropProceduresAtStart(false);
		final IFastOrm initial = FastOrmExerciser.Utils.makeInitial().withOptions(options);
		new FastOrmExerciser(new ExerciseNumbers(), //
				FastOrmExerciser.Utils.specWithBatch1_10_100_1000(initial), //
				new ChampionChallengerStatsExecutionVisitor(//
						FastOrmExerciser.Utils.getAllItemsCallback,//
						"Treat_Same", new IFunction1<IFastOrm, IFastOrm>() {
							@Override
							public IFastOrm apply(IFastOrm from) throws Exception {
								return from.withOptions(from.getContainer().getOptions().withOptimiseLeafAccess(false));
							}
						}, //
						"Optimise_Leaf", new IFunction1<IFastOrm, IFastOrm>() {
							@Override
							public IFastOrm apply(IFastOrm from) throws Exception {
								return from.withOptions(from.getContainer().getOptions().withOptimiseLeafAccess(true));
							}
						}));

	}
}
