package org.fastorm.stats;

import javax.sql.DataSource;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.IJob;
import org.fastorm.api.IJobOptimisations;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IEntityDefnParentChildVisitor;
import org.fastorm.reader.IEntityReader;
import org.fastorm.reader.impl.EntityReaderThin;
import org.fastorm.temp.impl.SqlHelper;
import org.fastorm.temp.impl.TempTableMakerFactory;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.callbacks.NoCallback;
import org.fastorm.utilities.maps.ISimpleMap;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class FastOrmExerciser {

	public static class Utils {
		public static final IJob makeInitial() {
			DataSource dataSource = new XmlBeanFactory(new ClassPathResource("MySqlDataSource.xml")).getBean(DataSource.class);
			IEntityDefn defn = IEntityDefn.Utils.parse(new TempTableMakerFactory(IJobOptimisations.Utils.usualBest()), new ClassPathResource("sample.xml"));
			return IJob.Utils.mySqlSingleThreaded(defn, dataSource);
		}

		public static Spec specWithBatch1_10_100_1000(final IJob initial) {
			final Spec initialSpec = new Spec(initial, //
					ISpecStage.Utils.batchSize(1, 10, 100, 1000)); //
			return initialSpec;
		}

		public static Spec specWithBatch1_10_100_1000_10000(final IJob initial) {
			final Spec initialSpec = new Spec(initial, //
					ISpecStage.Utils.batchSize(1, 10, 100, 1000, 10000)); //
			return initialSpec;
		}

		public static final ICallback<IJob> getAllItemsCallback = new ICallback<IJob>() {
			@Override
			@SuppressWarnings("unused")
			public void process(IJob t) throws Exception {
				for (ISimpleMap<String, Object> item : t.makeReader())
					;
			}
		};

		@SuppressWarnings("unused")
		public static void warmUp(int walmUpCount, final IJob job, ICallback<Integer> countCallback) throws Exception {
			IFastOrmContainer container = job.getContainer();
			new SqlHelper(container.getDataSource()).dropAllTables();
			MakeData.makeData(container, 100, ICallback.Utils.<Integer> noCallback());
			IEntityReader<ISimpleMap<String, Object>> reader = job.makeReader();
			for (int i = 0; i < walmUpCount; i++) {
				for (ISimpleMap<String, Object> item : reader)
					;
				countCallback.process(i);
			}
			System.out.println(walmUpCount);
		}
	}

	public FastOrmExerciser(ExerciseNumbers exerciseNumbers, Spec initialSpec, IFastOrmExecutorVisitor visitor) throws Exception {
		IJob initial = initialSpec.getInitialFastOrm();
		visitor.atStart(initialSpec, initial, exerciseNumbers);

		for (int outerRun = 0; outerRun < exerciseNumbers.outerRuns; outerRun++) {
			visitor.startOuterRun(outerRun);
			for (int databaseSize : exerciseNumbers.databaseSizes) {
				MakeData.makeData(initial.getContainer(), databaseSize, new NoCallback<Integer>());
				Spec spec = initialSpec.withDatabaseSize(databaseSize);
				visitor.startDatabase(outerRun, databaseSize, spec);
				for (final IJob job : spec) {
					IEntityDefn.Utils.walk(job.getEntityDefn(), new IEntityDefnParentChildVisitor() {
						SqlHelper helper = new SqlHelper(job.getContainer().getDataSource());

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
						visitor.innerRun(outerRun, databaseSize, spec, job, innerRun);
					}
					visitor.endTest(outerRun, databaseSize, spec, job);
				}
				visitor.endDatabase(outerRun, databaseSize);
			}
			visitor.endOuterRun(outerRun);
		}
	}

	public static void main(String[] args) throws Exception {
		final IFastOrmContainer initial = Utils.makeInitial().//
				withCreateAndDropAtStart(false).//
				withThinInterface(new EntityReaderThin()).//
				getContainer();
		Utils.warmUp(50, initial, ICallback.Utils.count);
		final Spec initialSpec = Utils.specWithBatch1_10_100_1000_10000(initial);
		// ISpecStage.Utils.indexSecondaryTables(false, true)//
		// ISpecStage.Utils.temporaryTables(false, true),
		new FastOrmExerciser(new ExerciseNumbers().withInnerRuns(1), initialSpec, new StatsExecutionVisitor(initialSpec, Utils.getAllItemsCallback));
	}

}
