package org.fastorm.utilities;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtiltiesTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllUtiltiesTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ListsTest.class);
		suite.addTestSuite(MapsTest.class);
		suite.addTestSuite(SimpleListsTest.class);
		suite.addTestSuite(CrossThreadsAggregatorTest.class);
		suite.addTestSuite(AbstractFindNextIterableTest.class);
		suite.addTestSuite(IterablesTest.class);
		//$JUnit-END$
		return suite;
	}

}
