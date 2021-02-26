package org.hibernate.search.bugs;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;

public abstract class SearchTestBase {
	private SessionFactory sessionFactory;
	
	@Before
	public void setUp() {
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
		MetadataSources sources = new MetadataSources(registryBuilder.build());
		Class<?>[] annotatedClasses = getAnnotatedClasses();
		if (annotatedClasses != null) {
			for (Class<?> entity : annotatedClasses) {
				sources.addAnnotatedClass(entity);
			}
		}

		Metadata metadata = sources.buildMetadata();

		final SessionFactoryBuilder builder = metadata.getSessionFactoryBuilder();
		this.sessionFactory = builder.build();
	}
	
	@After
	public void tearDown() {
		this.sessionFactory.close();
	}

	protected abstract Class<?>[] getAnnotatedClasses();
	
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
