package org.fastorm.constants;

public class FastOrmTestValues {

	// all values that will be a database table or column name should be lower case so that it will work on windows and Linux
	public final static String primaryTableName = "actualtablename";
	public final static String primaryTempTableName = "actualtemptablename";
	public final static String primaryIdColumn = "actualidcolumn";
	public final static String primaryIdType = "integer";
	public static final String primaryMaxLinesPerBatch = "1";

	public final static String childEntityName = "child";
	public final static String childTableName = "childtablename";
	public final static String childTempTableName = "childtemptablename";
	public final static String childIdColumn = "childidcolumn";
	public final static String childIdType = "integer";
	public final static String childLinkColumn = "childLinkValue";
	public static final String childMaxLinesPerBatch = "100";

}
