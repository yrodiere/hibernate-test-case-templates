package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ Product.class , ProductMetadata.class};
	}

	@Test
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			Product product1 = new Product( 1L, "Jane Smith" );
			Product product2 = new Product( 2L, "John Doe" );
	
			Transaction tx = s.beginTransaction();
			s.persist( product1 );
			s.persist( product2 );
			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			SearchSession searchSession = Search.session( session );

			List<Product> hits = searchSession.search( Product.class )
					.where( f -> f.match().field( "name" ).matching( "smith" ) )
					.fetchHits( 20 );

			assertEquals( 1, hits.size() );
			assertEquals( (Long) 1L, hits.get( 0 ).getId() );
		}
	}

}
