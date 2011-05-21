package org.fastorm.hibernateComparison;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

public class HelloHibernate {

	public static void main(String[] args) {
		System.out.println("Hello");

		SessionFactory factory = new AnnotationConfiguration().configure().buildSessionFactory();
		for (int run = 0; run < 10; run++) {
			long startTime = System.nanoTime();
			Session session = factory.getCurrentSession();
			Transaction tx = session.beginTransaction();

			Query createQuery = session.createQuery("from Person");
			@SuppressWarnings("unchecked")
			List<Person> people = createQuery.list();
			int count = 0;
			for (Person person : people) {
				for (@SuppressWarnings("unused")
				Address address : person.getAddresses())
					;
				for (@SuppressWarnings("unused")
				Telephone telephone : person.getTelephones())
					;
				count++;
			}

			double duration = (System.nanoTime() - startTime) / 1000000.0;
			System.out.println(String.format("DB: %10d Duration %10.2f", +count, duration));
			tx.commit();
		}
	}
}
