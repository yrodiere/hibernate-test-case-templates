package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ Root.class, Info.class, User.class };
	}

	@Test
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			Transaction tx = s.beginTransaction();

			Root root = new Root();
			root.setId( 1L );
			Info info = new Info();
			info.setId( 1L );
			root.setInfo( info );
			info.setRoot( root );
			s.persist( root );

			User user1 = new User();
			user1.setId( 1L );
			user1.setName( "one" );
			User user2 = new User();
			user2.setName( "two" );
			user2.setId( 2L );

			info.getUserList().add( user1 );
			user1.getInfoForUserList().add( info );
			info.setMainUser( user2 );
			user2.getInfoForMainUser().add( info );

			s.persist( user1 );
			s.persist( user2 );

			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			SearchSession searchSession = Search.session( session );

			assertThat( searchSession.search( Root.class )
					.where( f -> f.match().field( "info.mainUser.user_name" ).matching( "two" ) )
					.fetchHits( 20 ) )
					.hasSize( 1 )
					.element( 0 ).extracting( Root::getId )
					.isEqualTo( 1L );
			assertThat( searchSession.search( Root.class )
					.where( f -> f.match().field( "info.mainUser.user_name" ).matching( "three" ) )
					.fetchHits( 20 ) )
					.isEmpty();
		}

		try ( Session s = getSessionFactory().openSession() ) {
			Transaction tx = s.beginTransaction();

			Info info = s.load( Info.class, 1L );
			User user3 = new User();
			user3.setId( 3L );
			user3.setName( "three" );

			info.getMainUser().getInfoForMainUser().remove( info );
			info.setMainUser( user3 );
			user3.getInfoForMainUser().add( info );

			s.persist( user3 );

			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			SearchSession searchSession = Search.session( session );

			assertThat( searchSession.search( Root.class )
					.where( f -> f.match().field( "info.mainUser.user_name" ).matching( "two" ) )
					.fetchHits( 20 ) )
					.isEmpty();
			assertThat( searchSession.search( Root.class )
					.where( f -> f.match().field( "info.mainUser.user_name" ).matching( "three" ) )
					.fetchHits( 20 ) )
					.hasSize( 1 )
					.element( 0 ).extracting( Root::getId )
					.isEqualTo( 1L );
		}
	}

}
