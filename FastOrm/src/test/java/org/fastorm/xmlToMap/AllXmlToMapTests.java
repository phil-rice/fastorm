package org.fastorm.xmlToMap;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllXmlToMapTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllXmlToMapTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(MapToXmlAttributePrinterTest.class);
		suite.addTestSuite(MapToXmlElementPrinterTest.class);
		suite.addTestSuite(XmlToMapParserWithDefaultsTest.class);
		suite.addTestSuite(XmlToMapParserTest.class);
		// $JUnit-END$
		return suite;
	}

}
