package org.fastorm.stats;

import org.fastorm.api.IJob;
import org.fastorm.reader.impl.EntityReaderThin;
import org.fastorm.reader.impl.StoredProceduresEntityReaderThin;
import org.fastorm.utilities.functions.IFunction1;

public class CompareStoredProcedures {

	public static void main(String[] args) throws Exception {
		final IJob initial = FastOrmExerciser.Utils.makeInitial().withCreateAndDropProceduresAtStart(false);
		new FastOrmExerciser(new ExerciseNumbers(), //
				FastOrmExerciser.Utils.specWithBatch1_10_100_1000(initial), //
				new ChampionChallengerStatsExecutionVisitor(//
						FastOrmExerciser.Utils.getAllItemsCallback,//
						"Mysql.st", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withThinInterface(new EntityReaderThin());
							}
						}, //
						"StoredProcs", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withThinInterface(new StoredProceduresEntityReaderThin());
							}
						}));

	}
}
