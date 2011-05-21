package org.fastorm.constants;

public class FastOrmOptions {

	public final boolean addIndiciesToPrimaryTemporaryTables;
	public final boolean addIndiciesToIntermediateTemporaryTables;
	public final boolean useTemporaryTables;

	public FastOrmOptions() {
		this(true, true, true);

	}

	public FastOrmOptions(boolean addIndiciesToPrimaryTemporaryTables, boolean addIndiciesToIntermediateTemporaryTables, boolean useTemporaryTables) {
		this.addIndiciesToPrimaryTemporaryTables = addIndiciesToPrimaryTemporaryTables;
		this.addIndiciesToIntermediateTemporaryTables = addIndiciesToIntermediateTemporaryTables;
		this.useTemporaryTables = useTemporaryTables;
	}

	public static FastOrmOptions usualBest() {
		return new FastOrmOptions();
	}

	public static FastOrmOptions withOutTempTables() {
		return new FastOrmOptions().withTempTables(false);
	}

	public FastOrmOptions withTempTables(boolean useTemporaryTables) {
		return new FastOrmOptions(addIndiciesToPrimaryTemporaryTables, addIndiciesToIntermediateTemporaryTables, useTemporaryTables);
	}

}
