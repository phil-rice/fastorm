package org.fastorm.constants;

public class FastOrmOptions {

	public final boolean indexPrimaryTables;
	public final boolean indexSecondaryTables;
	public final boolean useTemporaryTables;
	public final int batchSize;
	public final int maxForOneThread;

	public FastOrmOptions() {
		this(true, 1000, 100, true, true);

	}

	public FastOrmOptions(boolean addIndiciesToPrimaryTemporaryTables, int batchSize, int maxForOneThread, boolean addIndiciesToIntermediateTemporaryTables, boolean useTemporaryTables) {
		this.indexPrimaryTables = addIndiciesToPrimaryTemporaryTables;
		this.batchSize = batchSize;
		this.maxForOneThread = maxForOneThread;
		this.indexSecondaryTables = addIndiciesToIntermediateTemporaryTables;
		this.useTemporaryTables = useTemporaryTables;
	}

	public static FastOrmOptions usualBest() {
		return new FastOrmOptions();
	}

	public static FastOrmOptions withOutTempTables() {
		return new FastOrmOptions().withTempTables(false);
	}

	public FastOrmOptions withTempTables(boolean useTemporaryTables) {
		return new FastOrmOptions(indexPrimaryTables, batchSize, maxForOneThread, indexSecondaryTables, useTemporaryTables);
	}

	public FastOrmOptions withBatchSize(int batchSize) {
		return new FastOrmOptions(indexPrimaryTables, batchSize, maxForOneThread, indexSecondaryTables, useTemporaryTables);
	}

	public FastOrmOptions withIndexSecondaryTables(boolean indexSecondaryTables) {
		return new FastOrmOptions(indexPrimaryTables, batchSize, maxForOneThread, indexSecondaryTables, useTemporaryTables);

	}

}
