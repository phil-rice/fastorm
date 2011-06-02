package org.fastorm.pooling.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPoolingImplTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPoolingImplTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(UnsafePoolTest.class);
		suite.addTestSuite(ArraySimpleMapPoolTest.class);
		suite.addTestSuite(ThreadSafePoolTest.class);
		//$JUnit-END$
		return suite;
	}

}
