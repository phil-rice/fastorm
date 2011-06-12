package org.fastorm.stats;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.IJob;
import org.fastorm.api.IJobOptimisations;
import org.fastorm.dataGenerator.DataGenerator;
import org.fastorm.dataGenerator.DataGeneratorVisitor;
import org.fastorm.dataGenerator.ExtraDataContributor;
import org.fastorm.dataGenerator.IDataGenerator;
import org.fastorm.dataGenerator.IRowGenerator;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.impl.SqlHelper;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.callbacks.ICallback;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MakeData {
	private static int dataSize = 1000;

	public static void main(String[] args) throws Exception {
		makeData(new ClassPathResource("MySqlDataSource.xml"), new ClassPathResource("sample.xml"), dataSize);
	}

	public static void makeData(Resource dataSourceResource, Resource defnResource, int dataSize) {
		makeData(dataSourceResource, defnResource, dataSize, ICallback.Utils.count);
	}

	public static void makeData(Resource dataSourceResource, Resource defnResource, int dataSize, ICallback<Integer> countCallback) {
		DataSource dataSource = new XmlBeanFactory(dataSourceResource).getBean(DataSource.class);
		IEntityDefn defn = IEntityDefn.Utils.parse(new TempTableMakerFactory(IJobOptimisations.Utils.usualBest()), defnResource);
		IFastOrmContainer fastOrm = IJob.Utils.mySqlSingleThreaded(defn, dataSource).getContainer();
		makeData(fastOrm, dataSize, countCallback);
		fastOrm.getContainer().shutdown();
	}

	public static void makeData(IFastOrmContainer fastOrm, int dataSize, final ICallback<Integer> eachTime) {
		ExtraDataContributor extraDataGenerator = new ExtraDataContributor();
		IEntityDefn.Utils.dropAndMakeTables(fastOrm, fastOrm.getEntityDefn(), extraDataGenerator);
		Map<IEntityDefn, IRowGenerator> defaultRowGenerators = IDataGenerator.Utils.findDefaultRowGeneratorsWithExtra(fastOrm, 20, extraDataGenerator);
		final SqlHelper helper = new SqlHelper(fastOrm.getDataSource());
		new DataGenerator().walkRandomData(defaultRowGenerators, fastOrm.getEntityDefn(), dataSize, new DataGeneratorVisitor() {
			int count;

			@Override
			public void acceptPrimary(Map<IEntityDefn, List<Map<String, Object>>> data) throws Exception {
				helper.insert(data);
				eachTime.process(count++);
			}
		});
		helper.index(fastOrm);
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
