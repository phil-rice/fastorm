package org.fastorm.pooling.api;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPoolingApiTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPoolingApiTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(StringPoolTest.class);
		//$JUnit-END$
		return suite;
	}

}
