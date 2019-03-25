package org.hibernate.search.bugs;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class YourTestCase extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ YourAnnotatedEntity.class };
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-3534")
	@SuppressWarnings("unchecked")
	public void noneOfShouldMatchedWithinBooleanQueryInsideFilter_differentResults_directoryBasedVSElasticsearch() {
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

}
