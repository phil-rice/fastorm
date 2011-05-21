package org.fastorm;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.fastorm.defns.AllDefnsTests;
import org.fastorm.reader.impl.AllReaderImplTests;
import org.fastorm.sqlDialects.AllSqlDialectTests;
import org.fastorm.temp.impl.AllTempImplTests;
import org.fastorm.utilities.AllUtiltiesTests;
import org.fastorm.xmlToMap.AllXmlToMapTests;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTest(AllUtiltiesTests.suite());
		suite.addTest(AllXmlToMapTests.suite());
		suite.addTest(AllDefnsTests.suite());
		suite.addTest(AllReaderImplTests.suite());
		suite.addTest(AllSqlDialectTests.suite());
		suite.addTest(AllTempImplTests.suite());
		// $JUnit-END$
		return suite;
	}

}
