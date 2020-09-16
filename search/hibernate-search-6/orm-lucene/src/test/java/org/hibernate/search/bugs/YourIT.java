package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchQuery;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.lucene.search.Explanation;

public class YourIT extends SearchTestBase {

	private final Logger log = LoggerFactory.getLogger( getClass().getName() );

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { Game.class };
	}

	@Test
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			Game yourEntity1 = new Game( 1L );
			Game yourEntity2 = new Game( 2L );
			yourEntity1.setName( "AmonsDu" );
			yourEntity2.setName( "US Army" );

			Transaction tx = s.beginTransaction();
			s.persist( yourEntity1 );
			s.persist( yourEntity2 );
			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			LuceneSearchQuery<Game> gameSearchQuery = fuzzySearch( session, "amonsdu" );

			SearchResult<Game> result = gameSearchQuery.fetch( 0, 2 );
			assertThat( result.hits() )
					.hasSize( 1 )
					.element( 0 ).extracting( Game::getId )
					.isEqualTo( 1L );

			Explanation explanation = gameSearchQuery.explain( 1L );
			log.info( explanation.toString() );
			log.info( result.hits().toString() );
		}
	}

	public LuceneSearchQuery<Game> fuzzySearch(EntityManager entityManager, String searchTerm) {
		SearchSession searchSession = Search.session( entityManager );
		return searchSession.search( Game.class )
				.extension( LuceneExtension.get() )
				.where( f -> f.bool()
						.should(
								f.match()
										.field( "name" )
										.matching( searchTerm )
										.fuzzy( 2, 0 )
										.boost( 4 )
						).should(
								f.phrase()
										.field( "description" )
										.matching( searchTerm )
										.slop( 3 )
										.boost( 1 )
						)
						.should(
								f.match()
										.fields( "platform", "platformId" )
										.matching( searchTerm )
						)
				).toQuery();
	}
}
