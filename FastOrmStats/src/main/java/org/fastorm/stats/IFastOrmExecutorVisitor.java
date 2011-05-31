package org.fastorm.stats;

import org.fastorm.api.IFastOrm;

interface IFastOrmExecutorVisitor {

	void atStart(Spec spec, IFastOrm initial, ExerciseNumbers numbers);

	void startOuterRun(int outerRun);

	void endOuterRun(int outerRun);

	void startDatabase(int outerRun, int databaseSize, Spec spec);

	void endDatabase(int outerRun, int databaseSize);

	void startTest(int outerRun, Spec spec);

	void endTest(int outerRun, int databaseSize, Spec spec, IFastOrm fastOrm);

	void innerRun(int outerRun, int databaseSize, Spec spec, IFastOrm fastOrm, int innerRun) throws Exception;

}