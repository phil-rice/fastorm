package org.fastorm.stats;

import org.fastorm.api.IJob;

interface IFastOrmExecutorVisitor {

	void atStart(Spec spec, IJob initial, ExerciseNumbers numbers);

	void startOuterRun(int outerRun);

	void endOuterRun(int outerRun);

	void startDatabase(int outerRun, int databaseSize, Spec spec);

	void endDatabase(int outerRun, int databaseSize);

	void startTest(int outerRun, Spec spec);

	void endTest(int outerRun, int databaseSize, Spec spec, IJob job);

	void innerRun(int outerRun, int databaseSize, Spec spec, IJob job, int innerRun) throws Exception;

}