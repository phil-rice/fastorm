package org.fastorm.constants;

public class FastOrmMessages {

	public static final String thisKeyCannotBeAnAttribute = "This key {0} cannot be an attribute, it must be an element";
	public static final String cannotHaveDefaultFor = "Key {0} is not present, and does not have a default";
	public static final String fastOrmFactoryIsNotConfiguredWith = "FastOrmFactory is not configured with {0}";
	public static final String cannotFindTableForEntityDefn = "Cannot find a table for {0} in the data set. Legal entity definitions are {1}";
	public static final String illegalKey = "Illegal key {0}. Legal values are {1}";

	// This are for printf
	public static final String sqlLoggerUpdated = "Update %10.2f %21d %s";
	public static final String sqlLoggerQueried = "Query  %10.2f %10.2f %10d %s";
	public static final String totalLoggerTime = " Total %10.2f";
	public static final String cannotAccessBeforeItHasBeenSetup = "Cannot access before it has been setup";
	public static final String cannotFindChildDataFor = "Cannot find child data for {0}";

}
