package org.fastorm.stats;

import static org.fastorm.stats.ISpecStage.Utils.batchSize;
import static org.fastorm.stats.ISpecStage.Utils.temporaryTables;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.impl.FastOrm;
import org.fastorm.utilities.collections.Iterables;

public class SpecTest extends TestCase {

	private FastOrm initial;

	public void testSpecsWithOneSpec() {
		checkBatchSize(Arrays.<Integer> asList(), batchSize());
		checkBatchSize(Arrays.asList(1, 2, 3), batchSize(1, 2, 3));
	}

	public void testSpecsWithTwoSpecs() {
		checkBatchSize(Arrays.<Integer> asList(), batchSize(), temporaryTables(false, true));
		checkBatchSize(Arrays.asList(1, 1, 2, 2, 3, 3), batchSize(1, 2, 3), temporaryTables(false, true));
		checkTempTable(Arrays.asList(false, true, false, true, false, true), batchSize(1, 2, 3), temporaryTables(false, true));
		checkBatchSize(Arrays.asList(1, 2, 3, 1, 2, 3), temporaryTables(false, true), batchSize(1, 2, 3));
		checkTempTable(Arrays.asList(false, false, false, true, true, true), temporaryTables(false, true), batchSize(1, 2, 3));
	}

	private void checkBatchSize(List<Integer> expected, ISpecStage... specs) {
		assertEquals(expected, Iterables.list(Iterables.map(new Spec(initial, specs), IFastOrm.Utils.getBatchSize())));
	}

	private void checkTempTable(List<Boolean> expected, ISpecStage... specs) {
		assertEquals(expected, Iterables.list(Iterables.map(new Spec(initial, specs), IFastOrm.Utils.getUseTemporaryTables())));
	}

	public void testTitle() {
		checkTitle("BatchSize UseTemp", null, batchSize(1), temporaryTables(true));
		checkTitle("DatabaseSize BatchSize UseTemp", 100, batchSize(1), temporaryTables(true));
	}

	private void checkTitle(String expected, Integer databaseSize, ISpecStage... stages) {
		assertEquals(expected, new Spec(initial, stages).withDatabaseSize(databaseSize).title());
	}

	public void testString() {
		checkString("      1000    true", null, batchSize(1), temporaryTables(true));
		checkString("         100       1000    true", 100, batchSize(1), temporaryTables(true));
	}

	private void checkString(String expected, Integer databaseSize, ISpecStage... stages) {
		assertEquals(expected, new Spec(initial, stages).withDatabaseSize(databaseSize).stringFor(initial));
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		initial = new FastOrm();
	}

}
