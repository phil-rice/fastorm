package org.fastorm;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.fastorm.defns.AllDefnsTests;
import org.fastorm.reader.impl.AllReaderImplTests;
import org.fastorm.sqlDialects.AllSqlDialectTests;
import org.fastorm.temp.impl.AllTempImplTests;
import org.fastorm.xmlToMap.AllXmlToMapTests;

public class AllFastOrmTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllFastOrmTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTest(AllXmlToMapTests.suite());
		suite.addTest(AllDefnsTests.suite());
		suite.addTest(AllReaderImplTests.suite());
		suite.addTest(AllSqlDialectTests.suite());
		suite.addTest(AllTempImplTests.suite());
		// $JUnit-END$
		return suite;
	}

}
