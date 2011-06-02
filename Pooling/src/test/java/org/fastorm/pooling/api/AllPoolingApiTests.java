package org.fastorm.pooling.api;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPoolingApiTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPoolingApiTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ByteBufferSimpleStringPoolTest.class);
		suite.addTestSuite(ByteArraySimpleStringPoolTest.class);
		suite.addTestSuite(ObjectPoolTest.class);
		//$JUnit-END$
		return suite;
	}

}
