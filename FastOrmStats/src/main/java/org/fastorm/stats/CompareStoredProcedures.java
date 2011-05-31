package org.fastorm.stats;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.IFastOrm;
import org.fastorm.reader.impl.EntityReaderThin;
import org.fastorm.reader.impl.StoredProceduresEntityReaderThin;
import org.fastorm.utilities.IFunction1;

public class CompareStoredProcedures {

	public static void main(String[] args) throws Exception {
		FastOrmOptions options = new FastOrmOptions().withCreateAndDropProceduresAtStart(false);
		final IFastOrm initial = FastOrmExerciser.Utils.makeInitial().withOptions(options);
		new FastOrmExerciser(new ExerciseNumbers(), //
				FastOrmExerciser.Utils.specWithBatch1_10_100_1000(initial), //
				new ChampionChallengerStatsExecutionVisitor(//
						FastOrmExerciser.Utils.getAllItemsCallback,//
						"Mysql.st", new IFunction1<IFastOrm, IFastOrm>() {
							@Override
							public IFastOrm apply(IFastOrm from) throws Exception {
								return from.withThinInterface(new EntityReaderThin());
							}
						}, //
						"StoredProcs", new IFunction1<IFastOrm, IFastOrm>() {
							@Override
							public IFastOrm apply(IFastOrm from) throws Exception {
								return from.withThinInterface(new StoredProceduresEntityReaderThin());
							}
						}));

	}
}
