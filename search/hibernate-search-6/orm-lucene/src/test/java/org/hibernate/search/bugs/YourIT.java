package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ YourAnnotatedEntity.class };
	}

	@Test
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			YourAnnotatedEntity yourEntity1 = new YourAnnotatedEntity( 1L, "Jane Smith" );
			yourEntity1.setSalesAmount( 5300L );
			YourAnnotatedEntity yourEntity2 = new YourAnnotatedEntity( 2L, "John Doe" );
			yourEntity2.setSalesAmount( 15_000L );
	
			Transaction tx = s.beginTransaction();
			s.persist( yourEntity1 );
			s.persist( yourEntity2 );
			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			SearchSession searchSession = Search.session( session );

			List<YourAnnotatedEntity> hits = searchSession.search( YourAnnotatedEntity.class )
					.where( f -> f.range().field( "salesAmount" )
							.between( 1L, 10_000L ) )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 1 )
					.element( 0 ).extracting( YourAnnotatedEntity::getId )
					.isEqualTo( 1L );

			hits = searchSession.search( YourAnnotatedEntity.class )
					.where( f -> f.range().field( "salesAmount" )
							.between( 10_000L, 53_000L ) )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 1 )
					.element( 0 ).extracting( YourAnnotatedEntity::getId )
					.isEqualTo( 2L );
		}
	}

}
