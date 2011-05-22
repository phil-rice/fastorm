package org.fastorm.reader.impl;

import javax.sql.DataSource;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.sql.SysOutSqlLogger;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class StoredProceduresEntityReaderThinTest extends AbstractEntityReaderTest {

	public void testMakesStoredProcedures() {

		DataSource dataSource = new XmlBeanFactory(new ClassPathResource("MySqlDataSource.xml")).getBean(DataSource.class);
		IEntityDefn defn = IEntityDefn.Utils.parse(new TempTableMakerFactory(), new ClassPathResource("sample.xml"));
		FastOrmOptions options = new FastOrmOptions().withBatchSize(10).withTempTables(false);
		IFastOrmContainer fastOrm = IFastOrm.Utils.mySqlSingleThreaded(defn, dataSource).withOptions(options).getContainer();
		StoredProceduresEntityReaderThin thin = new StoredProceduresEntityReaderThin();
		for (IDataSet dataSet : thin.dataSets(fastOrm.withSqlLogger(new SysOutSqlLogger()).getContainer()))
			System.out.println(dataSet);

	}
}
