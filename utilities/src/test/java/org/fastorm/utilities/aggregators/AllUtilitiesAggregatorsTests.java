package org.fastorm.utilities.aggregators;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtilitiesAggregatorsTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllUtilitiesAggregatorsTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(CrossThreadsAggregatorTest.class);
		//$JUnit-END$
		return suite;
	}

}
