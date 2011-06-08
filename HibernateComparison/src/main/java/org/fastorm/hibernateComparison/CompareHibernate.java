package org.fastorm.hibernateComparison;

import java.util.List;

import org.fastorm.api.IJob;
import org.fastorm.reader.impl.StoredProceduresEntityReaderThin;
import org.fastorm.stats.ChampionChallengerStatsExecutionVisitor;
import org.fastorm.stats.ChampionChallengerStatsExecutionVisitorRaw;
import org.fastorm.stats.ExerciseNumbers;
import org.fastorm.stats.FastOrmExerciser;
import org.fastorm.stats.IProfilable;
import org.fastorm.stats.ISpecStage;
import org.fastorm.stats.Spec;
import org.fastorm.utilities.functions.Functions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

public class CompareHibernate {

	public static void main(String[] args) throws Exception {
		IJob warmUp = FastOrmExerciser.Utils.makeInitial();
		// FastOrmExerciser.Utils.warmUp(1, warmUp.withOptions(new FastOrmOptions()), ICallback.Utils.<Integer> noCallback());

		final IJob initial = warmUp.withThinInterface(new StoredProceduresEntityReaderThin()).withCreateAndDropProceduresAtStart(false);
		new FastOrmExerciser(new ExerciseNumbers().withOuterRuns(1).withInnerRuns(1), //
				new Spec(initial, //
						ISpecStage.Utils.batchSize(100, 1, 10, 100, 1000)), //
				new ChampionChallengerStatsExecutionVisitorRaw(//
						"Hibernate", new IProfilable<IJob, SessionFactory>() {
							@Override
							public SessionFactory start(IJob situation) throws Exception {
								final SessionFactory factory = new AnnotationConfiguration().configure().buildSessionFactory();
								return factory;
							}

							@SuppressWarnings({ "unused", "unchecked" })
							@Override
							public void job(IJob situation, SessionFactory factory) throws Exception {
								Session session = factory.getCurrentSession();
								Transaction tx = session.beginTransaction();

								Query query = session.createQuery("from Person");
								List<Person> people = query.list();
								int count = 0;
								for (Person person : people) {
									count++;
									for (Address address : person.getAddresses())
										;
									for (Telephone telephone : person.getTelephones())
										for (Favourite favourite : telephone.getFavourites())
											;
								}
								System.out.println("Hib count: " + count);

								tx.commit();
							}

							@Override
							public void finish(IJob situation, SessionFactory factory) throws Exception {
								factory.close();
							}
						},//
						"FastOrm", ChampionChallengerStatsExecutionVisitor.toTime(FastOrmExerciser.Utils.getAllItemsCallback, Functions.<IJob, IJob> identity()) //
				));

	}
}
