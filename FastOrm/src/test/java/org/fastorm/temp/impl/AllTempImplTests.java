package org.fastorm.temp.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTempImplTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTempImplTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(TestAbstractTempTableMakerTest.class);
		suite.addTestSuite(AllEntitiesTempTableMakerTest.class);
		suite.addTestSuite(OneToManyTempTableMakerTest.class);
		suite.addTestSuite(MutatingTempTableMakerTest.class);
		//$JUnit-END$
		return suite;
	}

}
