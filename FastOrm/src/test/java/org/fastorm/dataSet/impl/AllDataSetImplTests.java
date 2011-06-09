package org.fastorm.dataSet.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDataSetImplTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllDataSetImplTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(DrainedLineCommonDataTest.class);
		suite.addTestSuite(DrainedLineTest.class);
		//$JUnit-END$
		return suite;
	}

}
