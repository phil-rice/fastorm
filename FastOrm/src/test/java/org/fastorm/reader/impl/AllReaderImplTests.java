package org.fastorm.reader.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllReaderImplTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllReaderImplTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(EntityReaderTest.class);
		suite.addTestSuite(StoredProceduresEntityReaderThinTest.class);
		suite.addTestSuite(AbstractEntityReaderTest.class);
		//$JUnit-END$
		return suite;
	}

}
