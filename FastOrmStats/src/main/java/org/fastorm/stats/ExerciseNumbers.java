package org.fastorm.stats;

import java.util.Arrays;
import java.util.List;

public class ExerciseNumbers {
	public final List<Integer> databaseSizes;
	public final int outerRuns;
	public final int innerRuns;

	public ExerciseNumbers() {
		this.databaseSizes = Arrays.asList(100, 1000, 10000);
		this.outerRuns = 1;
		this.innerRuns = 10;
	}

	public ExerciseNumbers(List<Integer> databaseSizes, int outerRuns, int innerRuns) {
		this.databaseSizes = databaseSizes;
		this.outerRuns = outerRuns;
		this.innerRuns = innerRuns;
	}

	public ExerciseNumbers withInnerRuns(int innerRuns) {
		return new ExerciseNumbers(databaseSizes, outerRuns, innerRuns);
	}

	public ExerciseNumbers withOuterRuns(int outerRuns) {
		return new ExerciseNumbers(databaseSizes, outerRuns, innerRuns);
	}
}