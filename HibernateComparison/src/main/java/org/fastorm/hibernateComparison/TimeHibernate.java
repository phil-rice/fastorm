package org.fastorm.hibernateComparison;

import java.util.List;

import org.fastorm.api.ICallback;
import org.fastorm.stats.MakeData;
import org.fastorm.utilities.Sets;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;
import org.springframework.core.io.ClassPathResource;

public class TimeHibernate {
	private static int[] dbSizes = new int[] { 1, 10, 100, 1000, 10000, 1, 10, 100, 1000, 10000, 1, 10, 100, 1000, 10000 };

	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) throws ClassNotFoundException {
		Sets.makeSet(1);// testing classes found

		for (int dbSize : dbSizes) {
			MakeData.makeData(new ClassPathResource("MySqlDataSource.xml"), new ClassPathResource("sample.xml"), dbSize, ICallback.Utils.<Integer> noCallback());
			SessionFactory factory = new AnnotationConfiguration().configure().buildSessionFactory();
			for (int run = 0; run < 10; run++) {
				long startTime = System.nanoTime();
				Session session = factory.getCurrentSession();
				Transaction tx = session.beginTransaction();

				Query createQuery = session.createQuery("from Person");
				List<Person> people = createQuery.list();
				for (Person person : people) {
					for (Address address : person.getAddresses())
						;
					for (Telephone telephone : person.getTelephones())
						for (Favourite favourite : telephone.getFavourites())
							;
				}

				double duration = (System.nanoTime() - startTime) / 1000000.0;
				System.out.println(String.format("DB: %10d Took %10.3f %10.3f", dbSize, duration, duration / dbSize));
				tx.commit();
			}
		}
	}
}
