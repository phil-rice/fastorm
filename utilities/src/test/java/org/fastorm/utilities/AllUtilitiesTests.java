package org.fastorm.utilities;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.fastorm.utilities.aggregators.AllUtilitiesAggregatorsTests;
import org.fastorm.utilities.collections.AllUtilitiesCollectionsTests;
import org.fastorm.utilities.maps.AllUtilitiesMapsTests;
import org.fastorm.utilities.reflection.AllUtilitiesReflectionTests;

public class AllUtilitiesTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllUtilitiesTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTest(AllUtilitiesAggregatorsTests.suite());
		suite.addTest(AllUtilitiesCollectionsTests.suite());
		suite.addTest(AllUtilitiesReflectionTests.suite());
		suite.addTest(AllUtilitiesMapsTests.suite());
		// $JUnit-END$
		return suite;
	}

}
