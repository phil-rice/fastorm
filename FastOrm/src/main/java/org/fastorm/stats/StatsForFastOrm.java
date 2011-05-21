package org.fastorm.stats;

import java.util.Arrays;
import java.util.Map;

import javax.sql.DataSource;

import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.impl.NoCallback;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.ArrayMemoryManager;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.memory.NoMemoryManager;
import org.fastorm.reader.IEntityReader;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.ISimpleMap;
import org.fastorm.utilities.Maps;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class StatsForFastOrm {

	private static IMemoryManager[] managers = new IMemoryManager[] { new NoMemoryManager(), new ArrayMemoryManager() };
	// private static int[] dataBaseSizes = new int[] { 1, 100 };
	// private static int[] batchSizes = new int[] { 1, 10, 100 };
	private static int[] dataBaseSizes = new int[] { 100, 1000, 10000 };
	private static int[] batchSizes = new int[] { 1, 10, 100, 1000, 10000 };
	private static int runs = 10;
	private static int walmUpCount = 1;

	public static interface ISpecWalker {
		void acceptMakeData(int databaseSize);

		void accept(Object key, Object keyForSize1, int databaseSize, int batchSize, IMemoryManager manager);
	}

	public static void walkSpecs(ISpecWalker walker) {
		for (int databaseSize : dataBaseSizes) {
			walker.acceptMakeData(databaseSize);
			for (IMemoryManager manager : managers) {
				for (int batchSize : batchSizes)
					if (batchSize <= databaseSize) {
						Object key = Arrays.asList(databaseSize, batchSize, manager);
						Object keyForSize1 = Arrays.asList(databaseSize, 1, manager);
						walker.accept(key, keyForSize1, databaseSize, batchSize, manager);
					}
			}
		}

	}

	public static void main(String[] args) throws Exception {
		DataSource dataSource = new XmlBeanFactory(new ClassPathResource("MySqlDataSource.xml", StatsForFastOrm.class)).getBean(DataSource.class);
		IEntityDefn defn = IEntityDefn.Utils.parse(new TempTableMakerFactory(), new ClassPathResource("sample.xml", StatsForFastOrm.class));
		final IFastOrmContainer fastOrm = (IFastOrmContainer) IFastOrm.Utils.mySqlSingleThreaded(defn, dataSource);
		warmUp(fastOrm);
		final Map<Object, Stats> statsMap = Maps.newMap();
		walkSpecs(new ISpecWalker() {
			@Override
			public void acceptMakeData(int databaseSize) {
				MakeData.makeData(fastOrm, databaseSize, new NoCallback());
			}

			@Override
			@SuppressWarnings("unused")
			public void accept(Object key, Object keyForSize1, int databaseSize, int batchSize, IMemoryManager manager) {
				displayTitle("");
				for (int run = 0; run < runs; run++) {
					IFastOrm toUse = fastOrm.withMemoryManager(manager).withDataSize(batchSize);
					IEntityReader<ISimpleMap<String, Object>> reader = toUse.makeReader();
					long startTime = System.currentTimeMillis();
					for (ISimpleMap<String, Object> item : reader)
						;
					long duration = System.currentTimeMillis() - startTime;
					Stats.add(statsMap, duration, key);
					System.out.println(String.format("%10d %10d %20s %10d  %10.2f", databaseSize, batchSize, manager.getClass().getSimpleName(), duration, duration * 1.0 / databaseSize));
				}
				displayAggregateTitle();
				displayAggregateStats(statsMap, key, keyForSize1, databaseSize, batchSize, manager);
				System.out.println();
			}
		});
		System.out.println();
		System.out.println("Results");
		displayAggregateTitle();

		walkSpecs(new ISpecWalker() {
			@Override
			public void acceptMakeData(int databaseSize) {
			}

			@Override
			public void accept(Object key, Object keyForSize1, int databaseSize, int batchSize, IMemoryManager manager) {
				displayAggregateStats(statsMap, key, keyForSize1, databaseSize, batchSize, manager);
			}
		});
	}

	private static void displayTitle(String postFix) {
		System.out.println("        DB      Batch      Manager           Duration     PerItem" + postFix);
	}

	private static void displayAggregateTitle() {
		displayTitle("         SD    PerItem  Speed up");
	}

	@SuppressWarnings("unused")
	private static void warmUp(final IFastOrmContainer fastOrm) {
		MakeData.makeData(fastOrm, 100, ICallback.Utils.count);
		IEntityReader<ISimpleMap<String, Object>> reader = fastOrm.makeReader();
		for (int i = 0; i < walmUpCount; i++) {
			for (ISimpleMap<String, Object> item : reader)
				;
			System.out.print(".");
		}
		System.out.println();
	}

	private static void displayAggregateStats(final Map<Object, Stats> statsMap, Object key, Object keyForSize1, int databaseSize, int batchSize, IMemoryManager manager) {
		Stats stats = statsMap.get(key);
		Stats forOne = statsMap.get(keyForSize1);
		long averageDuration = stats.averageDuration();
		double deviation = stats.standardDeviation();
		double averageDeviation = stats.standardDeviation() / stats.count;
		System.out.println(String.format("%10d %10d %20s %10d  %10.2f %10.2f %10.5f %10.5f", //
				databaseSize, batchSize, manager.getClass().getSimpleName(), //
				averageDuration, averageDuration * 1.0 / databaseSize,//
				deviation, averageDeviation * 1.0 / databaseSize,//
				averageDuration * 1.0 / forOne.averageDuration()));
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
