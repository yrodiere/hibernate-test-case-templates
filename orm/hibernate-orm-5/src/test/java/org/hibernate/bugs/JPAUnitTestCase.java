package org.hibernate.bugs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		try {
			// Do stuff...
			Application application = sampleApplication();
			entityManager.persist( application );
			entityManager.getTransaction().commit();
		} catch ( PersistenceException exception ) {
			entityManager.getTransaction().rollback();
			exception.printStackTrace();
		} finally {
			entityManager.close();
		}
	}

	private Application sampleApplication() {
		return new Application(
				ApplicationType.UNI5,
				MedicalRequirementType.B, MedicalRequirementType.A );
	}
}
