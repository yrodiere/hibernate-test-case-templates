package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.Test;


public class AnnoOnDifferentGetterNameTest extends SearchTestBase {

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] { Book.class };
    }

	@Test
	@TestForIssue(jiraKey = "HSEARCH-1078")
    public void testChangeToEntity() throws Exception {
        Book book = new Book("foo");
        Session s = getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.persist( book );
        tx.commit();

        FullTextSession session = Search.getFullTextSession( s );
        QueryParser parser = new QueryParser( "txtfld", new StandardAnalyzer() );
        Query query;
        List<?> result;


        query = parser.parse( "foo" );
        result = session.createFullTextQuery( query, Book.class ).list();
        assertEquals( "unable to find book with text 'foo'", 1, result.size() );

        s.clear();

        //Let's change the text of book
        tx = s.beginTransaction();

        book = ( Book ) s.get( Book.class, book.getId() );
        book.setText( "bar" );
        tx.commit();

        s.clear();

        session = Search.getFullTextSession( s );
        tx = s.beginTransaction();

        //check if the old value of book.text ('foo') is not matched anymore
        query = parser.parse( "foo" );
        result = session.createFullTextQuery( query, Book.class ).list();
        assertEquals( "change on simple string field not reflected in root index", 0, result.size() );

        //check if the new value of book.text ('bar') is matched
        query = parser.parse( "bar" );
        result = session.createFullTextQuery( query, Book.class ).list();
        assertEquals( "change on simple string field not reflected in root index", 1, result.size() );

        tx.commit();

        s.clear();
        s.close();
    }


}
