package org.fastorm.constants;

public class FastOrmMessages {

	public static final String needPositivePartitionSize = "Need partition size > 0. Have {0}";
	public static final String duplicateKey = "Cannot have duplicate value for key {0}. Existing value {1}. New value {2}";
	public static final String illegalKey = "Illegal key {0}. Legal values are {1}";
	public static final String thisKeyCannotBeAnAttribute = "This key {0} cannot be an attribute, it must be an element";
	public static final String cannotHaveDefaultFor = "Key {0} is not present, and does not have a default";
	public static final String fastOrmFactoryIsNotConfiguredWith = "FastOrmFactory is not configured with {0}";
	public static final String multipleUncaughtExceptions = "Multiple Uncaught Exceptions occured on the worker threads {0}";
	public static final String cannotHaveNullReturnedBySplitFunction = "Cannot have a null pointed returned by 'Iterables.split'. The split function class was {0}. The parameter was {1}";
	public static final String cannotHaveNullInIterableBeingProcessedBySplit = "Cannot Have Null In Iterable Being Processed By Split";
	public static final String cannotFindTableForEntityDefn = "Cannot find a table for {0} in the data set. Legal entity definitions are {1}";
	public static final String noValueForKey = "Could not find a value for the key {0} in map {1}";
	public static final String cannotFindChildTableWithName = "Cannot find a child {1} for parent {0}. Parent value is {2}";

	// This are for printf
	public static final String sqlLoggerUpdated = "Update %10.2f %21d %s";
	public static final String sqlLoggerQueried = "Query  %10.2f %10.2f %10d %s";
	public static final String totalLoggerTime = " Total %10.2f";

}
