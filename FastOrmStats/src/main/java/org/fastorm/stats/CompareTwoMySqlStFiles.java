package org.fastorm.stats;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.IFastOrm;
import org.fastorm.sqlDialects.SqlStrings;
import org.fastorm.utilities.functions.IFunction1;
import org.springframework.core.io.ClassPathResource;

public class CompareTwoMySqlStFiles {

	public static void main(String[] args) throws Exception {
		FastOrmOptions options = new FastOrmOptions().withCreateAndDropProceduresAtStart(false);
		final IFastOrm initial = FastOrmExerciser.Utils.makeInitial().withOptions(options);
		final SqlStrings championSqlStrings = new SqlStrings(new ClassPathResource("MySql.st"));
		final SqlStrings challangerSqlStrings = new SqlStrings(new ClassPathResource("MySql_To_Compare.st"));
		new FastOrmExerciser(new ExerciseNumbers(), //
				FastOrmExerciser.Utils.specWithBatch1_10_100_1000(initial),//
				new ChampionChallengerStatsExecutionVisitor(//
						FastOrmExerciser.Utils.getAllItemsCallback,//
						"Mysql.st", new IFunction1<IFastOrm, IFastOrm>() {
							@Override
							public IFastOrm apply(IFastOrm from) throws Exception {
								return from.withSqlDialect(championSqlStrings);
							}
						}, //
						"MySql_To_Compare.st", new IFunction1<IFastOrm, IFastOrm>() {
							@Override
							public IFastOrm apply(IFastOrm from) throws Exception {
								return from.withSqlDialect(challangerSqlStrings);
							}
						}));
	}
}
