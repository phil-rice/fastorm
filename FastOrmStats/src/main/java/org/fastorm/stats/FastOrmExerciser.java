package org.fastorm.stats;

import javax.sql.DataSource;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.impl.NoCallback;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IEntityDefnVisitor;
import org.fastorm.reader.IEntityReader;
import org.fastorm.reader.impl.StoredProceduresEntityReaderThin;
import org.fastorm.temp.impl.SqlHelper;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.ISimpleMap;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class FastOrmExerciser {

	public static class Utils {
		public static final IFastOrm makeInitial() {
			DataSource dataSource = new XmlBeanFactory(new ClassPathResource("MySqlDataSource.xml")).getBean(DataSource.class);
			IEntityDefn defn = IEntityDefn.Utils.parse(new TempTableMakerFactory(), new ClassPathResource("sample.xml"));
			return IFastOrm.Utils.mySqlSingleThreaded(defn, dataSource);
		}

		public static Spec specWithBatch1_10_100_1000(final IFastOrm initial) {
			final Spec initialSpec = new Spec(initial, //
					ISpecStage.Utils.batchSize(1, 10, 100, 1000)); //
			return initialSpec;
		}

		public static Spec specWithBatch1_10_100_1000_10000(final IFastOrm initial) {
			final Spec initialSpec = new Spec(initial, //
					ISpecStage.Utils.batchSize(1, 10, 100, 1000, 10000)); //
			return initialSpec;
		}

		public static final ICallback<IFastOrm> getAllItemsCallback = new ICallback<IFastOrm>() {
			@Override
			@SuppressWarnings("unused")
			public void process(IFastOrm t) throws Exception {
				for (ISimpleMap<String, Object> item : t.makeReader())
					;
			}
		};

		@SuppressWarnings("unused")
		public static void warmUp(int walmUpCount, final IFastOrm fastOrm, ICallback<Integer> countCallback) throws Exception {
			IFastOrmContainer container = fastOrm.getContainer();
			new SqlHelper(container.getJdbcTemplate()).dropAllTables();
			MakeData.makeData(container, 100, ICallback.Utils.<Integer> noCallback());
			IEntityReader<ISimpleMap<String, Object>> reader = fastOrm.makeReader();
			for (int i = 0; i < walmUpCount; i++) {
				for (ISimpleMap<String, Object> item : reader)
					;
				countCallback.process(i);
			}
			System.out.println(walmUpCount);
		}
	}

	public FastOrmExerciser(ExerciseNumbers exerciseNumbers, Spec initialSpec, IFastOrmExecutorVisitor visitor) throws Exception {
		IFastOrm initial = initialSpec.getInitialFastOrm();
		visitor.atStart(initialSpec, initial, exerciseNumbers);

		for (int outerRun = 0; outerRun < exerciseNumbers.outerRuns; outerRun++) {
			visitor.startOuterRun(outerRun);
			for (int databaseSize : exerciseNumbers.databaseSizes) {
				MakeData.makeData(initial.getContainer(), databaseSize, new NoCallback<Integer>());
				Spec spec = initialSpec.withDatabaseSize(databaseSize);
				visitor.startDatabase(outerRun, databaseSize, spec);
				for (final IFastOrm fastOrm : spec) {
					IEntityDefn.Utils.walk(fastOrm.getEntityDefn(), new IEntityDefnVisitor() {
						SqlHelper helper = new SqlHelper(fastOrm.getContainer().getJdbcTemplate());

						@Override
						public void acceptPrimary(IEntityDefn primary) throws Exception {
							helper.execute("drop table if exists " + primary.getTempTableName());
						}

						@Override
						public void acceptChild(IEntityDefn parent, IEntityDefn child) throws Exception {
							helper.execute("drop table if exists " + child.getTempTableName());
						}
					});
					visitor.startTest(outerRun, spec);
					for (int innerRun = 0; innerRun < exerciseNumbers.innerRuns; innerRun++) {
						visitor.innerRun(outerRun, databaseSize, spec, fastOrm, innerRun);
					}
					visitor.endTest(outerRun, databaseSize, spec, fastOrm);
				}
				visitor.endDatabase(outerRun, databaseSize);
			}
			visitor.endOuterRun(outerRun);
		}
	}

	public static void main(String[] args) throws Exception {
		final IFastOrmContainer initial = Utils.makeInitial().//
				withOptions(new FastOrmOptions().withCreateAndDropProceduresAtStart(false)).//
				withThinInterface(new StoredProceduresEntityReaderThin()).//
				getContainer();
		Utils.warmUp(50, initial, ICallback.Utils.count);
		final Spec initialSpec = Utils.specWithBatch1_10_100_1000_10000(initial);
		// ISpecStage.Utils.indexSecondaryTables(false, true)//
		// ISpecStage.Utils.temporaryTables(false, true),
		new FastOrmExerciser(new ExerciseNumbers().withInnerRuns(1), initialSpec, new StatsExecutionVisitor(initialSpec, Utils.getAllItemsCallback));
	}

}
