package org.fastorm.stats;

import javax.sql.DataSource;

import org.fastorm.api.IFastOrm;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.reader.IEntityReader;
import org.fastorm.sql.SysOutSqlLogger;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.maps.ISimpleMap;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class HelloFastOrm {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		Class.forName(MakeData.class.getName());
		DataSource dataSource = new XmlBeanFactory(new ClassPathResource("MySqlDataSource.xml")).getBean(DataSource.class);
		IEntityDefn defn = IEntityDefn.Utils.parse(new TempTableMakerFactory(), new ClassPathResource("sample.xml"));
		System.out.println("Starting");
		while (true) {
			int count = 0;
			// IFastOrmContainer fastOrm = IFastOrm.Utils.mySqlSingleThreaded(defn, dataSource).withDataSize(100).getContainer();
			IFastOrm fastOrm = IFastOrm.Utils.mySqlSingleThreaded(defn, dataSource).withBatchSize(100).//
					withSqlLogger(new SysOutSqlLogger());// .withThinInterface(new StoredProceduresEntityReaderThin());
			IEntityReader<ISimpleMap<String, Object>> reader = fastOrm.makeReader();
			long forStartTime = System.currentTimeMillis();
			for (ISimpleMap<String, Object> item : reader) {
				count++;
				// System.out.println(item);
			}
			System.out.println(count + " Took " + (System.currentTimeMillis() - forStartTime));

			fastOrm.getContainer().shutdown();
		}
	}
}
// EntityDefn addressDefn = new EntityDefn(Maps.<String, String> makeMap(//
// FastOrmKeys.entityName, "address", //
// FastOrmKeys.tableName, "address",//
// FastOrmKeys.idColumn, "a_id",//
// FastOrmKeys.versionColumn, "version", //
// FastOrmKeys.childLink, "a_person"), Collections.EMPTY_LIST);
// EntityDefn telephoneDefn = new EntityDefn(Maps.<String, String> makeMap(//
// FastOrmKeys.entityName, "telephone", //
// FastOrmKeys.tableName, "telephone", //
// FastOrmKeys.idColumn, "id", //
// FastOrmKeys.versionColumn, "version", //
// FastOrmKeys.childLink, "t_person"), Collections.EMPTY_LIST);
// EntityDefn employerDefn = new EntityDefn(Maps.<String, String> makeMap(//
// FastOrmKeys.entityName, "employer", //
// FastOrmKeys.tableName, "employer",//
// FastOrmKeys.idColumn, "id",//
// FastOrmKeys.versionColumn, "version", //
// FastOrmKeys.parentLink, "p_employer"), Collections.EMPTY_LIST);
// EntityDefn expectedDefn = new EntityDefn(//
// Maps.<String, String> makeMap(FastOrmKeys.entityName, "person", //
// FastOrmKeys.tableName, "person", //
// FastOrmKeys.idColumn, "id",//
// FastOrmKeys.versionColumn, "p_version"),//
// Arrays.<IEntityDefn> asList(addressDefn,//
// telephoneDefn,//
// employerDefn));
