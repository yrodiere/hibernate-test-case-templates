package org.hibernate.bugs;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a standalone test case for Hibernate ORM.  Although this is perfectly
 * acceptable as a reproducer, usage of ORMUnitTestCase is preferred!
 */
public class ORMStandaloneTestCase {

	private SessionFactory sf;

	@Before
	public void setup() {
		Configuration config = new Configuration()
				.configure()
				.addInputStream( getClass().getResourceAsStream( "/Foo.hbm.xml" ) );
		sf = config.buildSessionFactory();
	}

	// Add your tests, using standard JUnit.

	@Test
	public void hhh123Test() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		s.persist( new Foo() );
		tx.commit();
		s.close();
	}
}
