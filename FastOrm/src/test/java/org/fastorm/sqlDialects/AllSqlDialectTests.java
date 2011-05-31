package org.fastorm.sqlDialects;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllSqlDialectTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllSqlDialectTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(SqlStringsTest.class);
		//$JUnit-END$
		return suite;
	}

}
