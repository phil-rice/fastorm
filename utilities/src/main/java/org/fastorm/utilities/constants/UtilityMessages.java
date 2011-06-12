package org.fastorm.utilities.constants;

public class UtilityMessages {

	public static final String cannotHaveNullReturnedBySplitFunction = "Cannot have a null pointed returned by 'Iterables.split'. The split function class was {0}. The parameter was {1}";
	public static final String cannotHaveNullInIterableBeingProcessedBySplit = "Cannot Have Null In Iterable Being Processed By Split";
	public static final String needPositivePartitionSize = "Need partition size > 0. Have {0}";
	public static final String duplicateKey = "Cannot have duplicate value for key {0}. Existing value {1}. New value {2}";
	public static final String illegalKey = "Illegal key {0}. Legal values are {1}";
	public static final String cannotSetLength = "Cannot set length. Desired value {0}. Max value {1}";
	public static final String tooManyBytes = "Too large a parameter from. Parameter length is {0}. Max length is {1}";
	public static final String objectPoolFull = "Object pool is full. Asked for item index {0} and maximum is {1}. Object defn is {2}";

}
