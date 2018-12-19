package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.ngram.NGramTokenizerFactory;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.Test;

public class YourTestCase extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ RegistryReference.class, Staff.class };
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-3457") // Please fill in the JIRA key of your issue
	@SuppressWarnings("unchecked")
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			RegistryReference reference = new RegistryReference();
			reference.id = 1L;
			reference.unstructuredAddressDescription = "this is some address";
			Staff staff = new Staff();
			staff.id = 2L;

			Transaction tx = s.beginTransaction();
			s.persist( reference );
			s.persist( staff );
			tx.commit();

			FullTextSession session = Search.getFullTextSession( s );
			QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity( RegistryReference.class ).get();
			Query query = qb.all().createQuery();
			Sort sort = qb.sort().byField( "sort-address" ).andByField( "sort-date" ).createSort();
	
			List<RegistryReference> result = (List<RegistryReference>) session.createFullTextQuery( query )
					.setSort( sort )
					.list();
			assertEquals( 1, result.size() );
			assertEquals( 1L, result.get( 0 ).id );
		}
	}
	@Indexed
	@Entity
	public static class Staff {
		@Id
		private long id;
	}

	@AnalyzerDefs({
			@AnalyzerDef(name = "ngram", tokenizer = @TokenizerDef(factory = NGramTokenizerFactory.class)),
			@AnalyzerDef(name = "plain", tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class))
	})
	@Indexed
	@Entity
	public static class RegistryReference {
		@Id
		private long id;

		@Field(name = "sort-date", index = Index.NO, analyze = Analyze.NO)
		@SortableField(forField = "sort-date")
		@DateBridge(resolution = Resolution.MILLISECOND)
		@Transient
		@SuppressWarnings("unused")
		private Date getIndexSortDate() {
			return new Date();
		}

		@Fields({
				@Field(name = "unstructured-address-ngram", analyzer = @Analyzer(definition = "ngram")),
				@Field(name = "unstructured-address-plain", analyzer = @Analyzer(definition = "plain")),
				@Field(name = "sort-address", index = Index.NO, analyze = Analyze.NO)
		})
		@SortableField(forField = "sort-address")
		private String unstructuredAddressDescription;

	}
}
