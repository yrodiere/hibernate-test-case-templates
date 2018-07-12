package org.hibernate.bugs;

import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.testing.transaction.TransactionUtil;
import org.junit.Assert;
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
		StandardServiceRegistryBuilder srb = new StandardServiceRegistryBuilder()
			.applySetting( "hibernate.show_sql", "true" )
			.applySetting( "hibernate.format_sql", "true" )
			.applySetting( "hibernate.hbm2ddl.auto", "update" );

		Metadata metadata = new MetadataSources( srb.build() )
				.addAnnotatedClass( CsvDataSourceResource.class )
				.addAnnotatedClass( DataBaseSourceResource.class )
				.addAnnotatedClass( DataSourceResource.class )
				.addAnnotatedClass( JPAAccessibleResource.class )
				.buildMetadata();

		sf = metadata.buildSessionFactory();
	}

	// Add your tests, using standard JUnit.

	@Test
	public void hhh12641Test() throws Exception {
		AtomicReference<Long> id = new AtomicReference<>();

		TransactionUtil.doInHibernate( this::getSessionFactory, s -> {
			CsvDataSourceResource entity = new CsvDataSourceResource();
			entity.setProtocol( "http" );
			entity.setHost( "localhost" );
			entity.setPath( "path/to/something" );
			entity.setPort( 4242 );
			entity.setUsername( "mimidu68" );
			entity.setPassword( "hunter12" );
			s.persist( entity );
			id.set( entity.getId() );
		} );

		TransactionUtil.doInHibernate( this::getSessionFactory, s -> {
			CsvDataSourceResource entity = s.find( CsvDataSourceResource.class, id.get() );
			Assert.assertNotNull( entity );
		} );
	}

	private SessionFactory getSessionFactory() {
		return sf;
	}

}
