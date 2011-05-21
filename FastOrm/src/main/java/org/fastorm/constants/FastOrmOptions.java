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

	public FastOrmOptions(boolean indexPrimaryTables, int batchSize, int maxForOneThread, boolean indexSecondaryTables, boolean useTemporaryTables) {
		this.indexPrimaryTables = indexPrimaryTables;
		this.batchSize = batchSize;
		this.maxForOneThread = maxForOneThread;
		this.indexSecondaryTables = indexSecondaryTables;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + batchSize;
		result = prime * result + (indexPrimaryTables ? 1231 : 1237);
		result = prime * result + (indexSecondaryTables ? 1231 : 1237);
		result = prime * result + maxForOneThread;
		result = prime * result + (useTemporaryTables ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FastOrmOptions other = (FastOrmOptions) obj;
		if (batchSize != other.batchSize)
			return false;
		if (indexPrimaryTables != other.indexPrimaryTables)
			return false;
		if (indexSecondaryTables != other.indexSecondaryTables)
			return false;
		if (maxForOneThread != other.maxForOneThread)
			return false;
		if (useTemporaryTables != other.useTemporaryTables)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FastOrmOptions [indexPrimaryTables=" + indexPrimaryTables + ", indexSecondaryTables=" + indexSecondaryTables + ", useTemporaryTables=" + useTemporaryTables + ", batchSize="
				+ batchSize + ", maxForOneThread=" + maxForOneThread + "]";
	}

	public FastOrmOptions withMaxInOneThread(int maxForOneThread) {
		return new FastOrmOptions(indexPrimaryTables, batchSize, maxForOneThread, indexSecondaryTables, useTemporaryTables);
	}
}
