package org.fastorm.utilities.strings;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtilitiesStringStringTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllUtilitiesStringStringTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(AbstractSimpleStringTest.class);
		// $JUnit-END$
		return suite;
	}

}
