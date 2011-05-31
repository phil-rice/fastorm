package org.fastorm.hibernateComparison;

import java.util.List;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.ICallback;
import org.fastorm.api.IFastOrm;
import org.fastorm.reader.impl.StoredProceduresEntityReaderThin;
import org.fastorm.stats.ChampionChallengerStatsExecutionVisitor;
import org.fastorm.stats.ChampionChallengerStatsExecutionVisitorRaw;
import org.fastorm.stats.ExerciseNumbers;
import org.fastorm.stats.FastOrmExerciser;
import org.fastorm.stats.IProfilable;
import org.fastorm.stats.ISpecStage;
import org.fastorm.stats.Spec;
import org.fastorm.utilities.Functions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

public class CompareHibernate {

	public static void main(String[] args) throws Exception {
		IFastOrm warmUp = FastOrmExerciser.Utils.makeInitial();
		FastOrmExerciser.Utils.warmUp(1, warmUp.withOptions(new FastOrmOptions()), ICallback.Utils.<Integer> noCallback());

		FastOrmOptions options = new FastOrmOptions().withCreateAndDropProceduresAtStart(false);
		final IFastOrm initial = warmUp.withOptions(options).withThinInterface(new StoredProceduresEntityReaderThin());
		new FastOrmExerciser(new ExerciseNumbers().withOuterRuns(10), //
				new Spec(initial, //
						ISpecStage.Utils.batchSize(100, 1, 10, 100, 1000)), //
				new ChampionChallengerStatsExecutionVisitorRaw(//
						"Hibernate", new IProfilable<IFastOrm, SessionFactory>() {
							@Override
							public SessionFactory start(IFastOrm situation) throws Exception {
								final SessionFactory factory = new AnnotationConfiguration().configure().buildSessionFactory();
								return factory;
							}

							@Override
							public void job(IFastOrm situation, SessionFactory factory) throws Exception {
								Session session = factory.getCurrentSession();
								Transaction tx = session.beginTransaction();

								Query query = session.createQuery("from Person");
								List<Person> people = query.list();
								for (Person person : people) {
									for (Address address : person.getAddresses())
										;
									for (Telephone telephone : person.getTelephones())
										for (Favourite favourite : telephone.getFavourites())
											;
								}

								tx.commit();
							}

							@Override
							public void finish(IFastOrm situation, SessionFactory factory) throws Exception {
								factory.close();
							}
						},//
						"FastOrm", ChampionChallengerStatsExecutionVisitor.toTime(FastOrmExerciser.Utils.getAllItemsCallback, Functions.<IFastOrm, IFastOrm> identity()) //
				));

	}
}
