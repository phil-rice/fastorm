package org.fastorm.stats;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllStatsTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllStatsTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(SpecTest.class);
		//$JUnit-END$
		return suite;
	}

}
