package org.fastorm.stats;

import org.fastorm.api.IJob;
import org.fastorm.sqlDialects.SqlStrings;
import org.fastorm.utilities.functions.IFunction1;
import org.springframework.core.io.ClassPathResource;

public class CompareTwoMySqlStFiles {

	public static void main(String[] args) throws Exception {
		final IJob initial = FastOrmExerciser.Utils.makeInitial().withCreateAndDropAtStart(false);
		final SqlStrings championSqlStrings = new SqlStrings(new ClassPathResource("MySql.st"));
		final SqlStrings challangerSqlStrings = new SqlStrings(new ClassPathResource("MySql_To_Compare.st"));
		new FastOrmExerciser(new ExerciseNumbers(), //
				FastOrmExerciser.Utils.specWithBatch1_10_100_1000(initial),//
				new ChampionChallengerStatsExecutionVisitor(//
						FastOrmExerciser.Utils.getAllItemsCallback,//
						"Mysql.st", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withSqlDialect(championSqlStrings);
							}
						}, //
						"MySql_To_Compare.st", new IFunction1<IJob, IJob>() {
							@Override
							public IJob apply(IJob from) throws Exception {
								return from.withSqlDialect(challangerSqlStrings);
							}
						}));
	}
}
