package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.Test;

public class YourTestCase extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ QuotationEntity.class, QuotationLineEntity.class, QuotationItemEntity.class, QuotationArcticleEntity.class };
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-NNNNN") // Please fill in the JIRA key of your issue
	@SuppressWarnings("unchecked")
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			QuotationEntity root = new QuotationEntity();
			root.setId( 0 );

			QuotationItemEntity element1 = new QuotationItemEntity();
			element1.setId( 1 );
			element1.setQuotation( root );
			root.getQuotationLines().add( element1 );

			QuotationArcticleEntity element2 = new QuotationArcticleEntity();
			element2.setId( 2 );
			element2.setDesignation( "example" );
			element2.setQuotation( root );
			root.getQuotationLines().add( element2 );
	
			Transaction tx = s.beginTransaction();
			s.persist( root );
			tx.commit();
	
			FullTextSession session = Search.getFullTextSession( s );
			QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity( QuotationArcticleEntity.class ).get();
			Query query = qb.keyword().onField( "designation" ).matching( "example" ).createQuery();
	
			List<QuotationArcticleEntity> result = (List<QuotationArcticleEntity>) session.createFullTextQuery( query ).list();
			assertEquals( 1, result.size() );
			assertEquals( 2, (int) result.get( 0 ).getId() );
		}
	}

	@Indexed(index = "quotation")
	@Entity(name = "quotation")
	@Table
	public static class QuotationEntity {
		@Id
		private Integer id;

		@IndexedEmbedded
		@OneToMany(mappedBy = "quotation", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
		private Set<QuotationLineEntity> quotationLines = new HashSet<>();

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Set<QuotationLineEntity> getQuotationLines() {
			return quotationLines;
		}
	}

	@Entity(name = "quotation_line")
	@Table
	public static abstract class QuotationLineEntity {
		@Id
		private Integer id;

		@ManyToOne
		private QuotationEntity quotation;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public QuotationEntity getQuotation() {
			return quotation;
		}

		public void setQuotation(QuotationEntity quotation) {
			this.quotation = quotation;
		}
	}

	@Indexed(index = "quotation_article")
	@Entity(name = "quotation_article")
	@Table
	public static class QuotationArcticleEntity extends QuotationLineEntity {
		@Field
		@Column(name = "designation")
		private String designation;

		public String getDesignation() {
			return designation;
		}

		public void setDesignation(String designation) {
			this.designation = designation;
		}
	}

	@Entity(name = "quotation_item")
	@Table
	public static class QuotationItemEntity extends QuotationLineEntity {
	}
}
