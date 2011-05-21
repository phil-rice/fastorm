package org.fastorm.defns;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDefnsTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllDefnsTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(EntityDefnParserTest.class);
		// $JUnit-END$
		return suite;
	}

}
