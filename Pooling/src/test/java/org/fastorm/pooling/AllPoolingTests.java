package org.fastorm.pooling;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.fastorm.pooling.api.AllPoolingApiTests;
import org.fastorm.pooling.impl.AllPoolingImplTests;

public class AllPoolingTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPoolingTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTest(AllPoolingApiTests.suite());
		suite.addTest(AllPoolingImplTests.suite());
		// $JUnit-END$
		return suite;
	}

}
