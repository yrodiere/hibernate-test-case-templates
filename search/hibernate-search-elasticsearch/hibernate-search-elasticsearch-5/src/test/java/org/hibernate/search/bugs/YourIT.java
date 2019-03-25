package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.elasticsearch.ElasticsearchQueries;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.QueryDescriptor;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.After;
import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ YourAnnotatedEntity.class };
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-NNNNN") // Please fill in the JIRA key of your issue
	@SuppressWarnings("unchecked")
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			YourAnnotatedEntity yourEntity = new YourAnnotatedEntity( 1L, "goran", "jaric" );

			Transaction tx = s.beginTransaction();
			s.persist( yourEntity );
			tx.commit();

			FullTextSession session = Search.getFullTextSession( s );
			QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity( YourAnnotatedEntity.class ).get();
			Query query = qb.bool().must(qb.bool()
					.must(new TermQuery(new Term("id", "1"))).createQuery())
					.filteredBy(qb.bool()
							.should(new TermQuery(new Term("firstName", "nonexisting")))
							.must(new TermQuery(new Term("lastName", "jaric")))
									.createQuery()
					)
					.createQuery();
			List<YourAnnotatedEntity> result = (List<YourAnnotatedEntity>) session.createFullTextQuery( query ).list();
			assertEquals( 1, result.size() );
		}
	}

	@After
	public void deleteTestData() {
		try ( Session s = getSessionFactory().openSession() ) {
			FullTextSession session = Search.getFullTextSession( s );
			Transaction tx = s.beginTransaction();
	
			QueryDescriptor query = ElasticsearchQueries.fromJson( "{ 'query': { 'match_all' : {} } }" );
			List<?> result = session.createFullTextQuery( query, YourAnnotatedEntity.class ).list();
	
			for ( Object entity : result ) {
				session.delete( entity );
			}
	
			tx.commit();
		}
	}

}
