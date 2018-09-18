package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.Test;

public class YourTestCase extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{
				Client.class, ProfessionnelGC.class, Groupe.class
		};
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-2194") // Please fill in the JIRA key of your issue
	@SuppressWarnings("unchecked")
	public void testYourBug() {
		String raisonSociale = "Groupe truc";
		try ( Session session = getSessionFactory().openSession() ) {
			Transaction transaction = session.beginTransaction();
			Client client = new Client();
			client.id = 1;
			ProfessionnelGC prof = new ProfessionnelGC();
			client.professionnelGC = prof;
			prof.id = 2;
			Groupe groupe = new Groupe();
			prof.groupe = groupe;
			groupe.id = 3;
			groupe.numeroContrat = "2132424";
			groupe.raisonSociale = raisonSociale;
			groupe.sapId = "252315236436";
			session.persist( groupe );
			session.persist( prof );
			session.persist( client );
			transaction.commit();
		}
		try ( Session session = getSessionFactory().openSession();
				FullTextSession fullTextSession = Search.getFullTextSession( session ) ) {
			QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity( Client.class ).get();
			Query q = qb.keyword().onField( "professionnelGC.groupe.raisonSociale" ).matching( raisonSociale )
					.createQuery();
			FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery( q, Client.class );
			assertEquals( 1, fullTextQuery.getResultSize() );
		}
	}

	@Entity
	@Indexed
	public static class Client {
		@Id
		private Integer id;
		@ManyToOne
		@IndexedEmbedded
		private ProfessionnelGC professionnelGC;
	}

	@Entity
	@Indexed
	public static class ProfessionnelGC implements Serializable {
		@Id
		private Integer id;
		@ManyToOne
		@IndexedEmbedded(includePaths = { "raisonSociale" })
		private Groupe groupe;
	}

	@Entity
	@Indexed
	public static class Groupe {
		@Id
		private Integer id;

		@Field
		private String numeroContrat;

		@Field
		private String sapId;

		@Field(analyze = Analyze.NO)
		private String raisonSociale;

	}
}
