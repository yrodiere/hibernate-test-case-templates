package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.Test;

@TestForIssue(jiraKey = "HHH-13052")
public class YourTestCase extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ MyEntity.class, MyIdClass.class };
	}

	@Test
	@SuppressWarnings("unchecked")
	public void defaultRetrievalMethod() {
		doTest( null, null );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void queryRetrievalMethod() {
		doTest( ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY );
	}
	@Test
	@SuppressWarnings("unchecked")
	public void findByIdRetrievalMethod() {
		doTest( null, DatabaseRetrievalMethod.FIND_BY_ID );
	}

	public void doTest(ObjectLookupMethod objectLookupMethod, DatabaseRetrievalMethod databaseRetrievalMethod) {
		try ( Session s = getSessionFactory().openSession() ) {
			MyEntity myEntity1 = new MyEntity( new MyIdClass( 1, 100 ), "value1" );
			MyEntity myEntity2 = new MyEntity( new MyIdClass( 2, 200 ), "value2" );
	
			Transaction tx = s.beginTransaction();
			s.persist( myEntity1 );
			s.persist( myEntity2 );
			tx.commit();
		}

		// Open a new session to avoid reusing cached entities
		try ( Session s = getSessionFactory().openSession() ) {
			FullTextSession session = Search.getFullTextSession( s );
			QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity( MyEntity.class ).get();
			Query query = qb.all().createQuery();

			FullTextQuery fullTextQuery = session.createFullTextQuery( query );
			fullTextQuery.setSort( qb.sort().byField( "sortField" ).createSort() );

			if ( objectLookupMethod != null || databaseRetrievalMethod != null ) {
				fullTextQuery.initializeObjectsWith( objectLookupMethod, databaseRetrievalMethod );
			}

			List<MyEntity> result = (List<MyEntity>) fullTextQuery.list();
			assertEquals( 2, result.size() );
			MyIdClass result1Id = result.get( 0 ).getId();
			MyIdClass result2Id = result.get( 1 ).getId();

			assertEquals( (Integer) 1, result1Id.getMyField1() );
			assertEquals( (Integer) 100, result1Id.getMyField2() );
			assertEquals( (Integer) 2, result2Id.getMyField1() );
			assertEquals( (Integer) 200, result2Id.getMyField2() );

			assertEquals( (Integer) 101, result1Id.getMyTransientField() );
			assertEquals( (Integer) 202, result2Id.getMyTransientField() );
		}
	}

}
