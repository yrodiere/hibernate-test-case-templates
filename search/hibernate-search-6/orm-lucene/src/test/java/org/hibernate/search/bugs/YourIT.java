package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { A.class, B.class, C.class };
	}

	@Test
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			Transaction tx = s.beginTransaction();

			A a1 = new A( 1L );
			s.persist( a1 );
			B b1 = new B( a1, 1 );
			a1.getBList().add( b1 );
			s.persist( b1 );
			C c1_1 = new C( b1, 1 );
			b1.getCList().add( c1_1 );
			s.persist( c1_1 );
			C c1_2 = new C( b1, 2 );
			b1.getCList().add( c1_2 );
			s.persist( c1_2 );

			A a2 = new A( 1L );
			B b2 = new B( a2, 2 );
			a2.getBList().add( b2 );
			s.persist( b2 );
			C c2_1 = new C( b2, 1 );
			b2.getCList().add( c2_1 );
			s.persist( c2_1 );
			C c2_2 = new C( b2, 2 );
			b2.getCList().add( c2_2 );
			s.persist( c2_2 );

			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			SearchSession searchSession = Search.session( session );

			SearchScope<A> scope = searchSession.scope( A.class );
			SearchPredicateFactory pf = scope.predicate();

			SearchPredicate searchPredicate = pf.nested().objectField( "bList" )
					.nest( pf.bool()
							.must( pf.match().field( "bList.testId" ).matching( BigInteger.valueOf( 1 ) ) )
							.mustNot( pf.match().field( "bList.cList.type" ).matching( BigInteger.valueOf( 1 ) ) )
					)
					.toPredicate();

			List<A> hits = searchSession.search( A.class )
					.where( searchPredicate )
					.fetchHits( 20 );

			assertThat( hits )
					.isEmpty();
		}
	}

}
