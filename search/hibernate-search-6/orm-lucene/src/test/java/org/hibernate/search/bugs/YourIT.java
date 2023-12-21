package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { X.class, Y.class };
	}

	@Test
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			Y y1 = new Y( 1L, "Jane Smith" );
			X x1 = new X(1L, y1);
			Y y2 = new Y( 2L, "John Doe" );
			X x2 = new X(2L, y2);
			Y y3 = new Y( 3L, "Kevin Smith" );
			X x3 = new X(3L, y3);

			Transaction tx = s.beginTransaction();
			s.persist( y1 );
			s.persist( x1 );
			s.persist( y2 );
			s.persist( x2 );
			s.persist( y3 );
			s.persist( x3 );
			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			SearchSession searchSession = Search.session( session );

			List<X> hits = searchSession.search( X.class )
					.where( f -> f.match().field( "y.text" ).matching( "smith" ) )
					.loading( o -> {
						RootGraph<X> graph = session.createEntityGraph( X.class );
						graph.addAttributeNode( "y" );
						o.graph( graph, GraphSemantic.LOAD );
					} )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 2 )
					.extracting( X::getId )
					.containsExactlyInAnyOrder( 1L, 3L );
		}
	}

}
