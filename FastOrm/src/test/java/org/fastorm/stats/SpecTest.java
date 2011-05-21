package org.fastorm.stats;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.impl.FastOrm;
import org.fastorm.utilities.Iterables;

public class SpecTest extends TestCase {

	private FastOrm initial;
	private Spec spec;

	public void testSpecsWithOneSpec() {
		checkBatchSize(Arrays.<Integer> asList(), Spec.batchSize());
		checkBatchSize(Arrays.asList(1, 2, 3), Spec.batchSize(1, 2, 3));
	}

	public void testSpecsWithTwoSpecs() {
		checkBatchSize(Arrays.<Integer> asList(), Spec.batchSize(), Spec.temporaryTables(false, true));
		checkBatchSize(Arrays.asList(1, 1, 2, 2, 3, 3), Spec.batchSize(1, 2, 3), Spec.temporaryTables(false, true));
		checkTempTable(Arrays.asList(false, true, false, true, false, true), Spec.batchSize(1, 2, 3), Spec.temporaryTables(false, true));
		checkBatchSize(Arrays.asList(1, 2, 3, 1, 2, 3), Spec.temporaryTables(false, true), Spec.batchSize(1, 2, 3));
		checkTempTable(Arrays.asList(false, false, false, true, true, true), Spec.temporaryTables(false, true), Spec.batchSize(1, 2, 3));

	}

	private void checkBatchSize(List<Integer> expected, SpecStage<?>... specs) {
		Iterable<IFastOrm> test = spec.test(initial, specs);
		assertEquals(expected, Iterables.list(Iterables.map(test, IFastOrm.Utils.getBatchSize())));
	}

	private void checkTempTable(List<Boolean> expected, SpecStage<?>... specs) {
		Iterable<IFastOrm> test = spec.test(initial, specs);
		assertEquals(expected, Iterables.list(Iterables.map(test, IFastOrm.Utils.getUseTemporaryTables())));
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		initial = new FastOrm();
		spec = new Spec();
	}

}
