package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.impl.BuiltinIterableBridge;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;

import org.junit.Before;
import org.junit.Test;

public class YourTestCase extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { Containing.class, Contained.class };
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		prepareData();
	}

	private void prepareData() {
		try ( Session session = getSessionFactory().openSession() ) {
			Transaction tx = session.beginTransaction();

			Containing containing = new Containing();
			containing.setId( 0L );

			Contained contained1 = new Contained();
			contained1.setId( 1L );
			contained1.getStringList().add( "foo" );
			contained1.setContaining( containing );
			containing.getContained().add( contained1 );

			Contained contained2 = new Contained();
			contained2.setId( 2L );
			contained2.getStringList().add( "bar" );
			contained2.setContaining( containing );
			containing.getContained().add( contained2 );

			session.persist( contained1 );
			session.persist( contained2 );
			session.persist( containing );

			tx.commit();
		}
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-3358")
	public void update() throws Exception {
		try ( Session session = getSessionFactory().openSession() ) {
			Transaction tx = session.beginTransaction();
			List<Containing> results = findResults( session, "contained.stringList", "foobar" );
			assertEquals( 0, results.size() );
			tx.rollback();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			Transaction tx = session.beginTransaction();

			// Trigger an reindexing of "containing", which should indirectly trigger reads on "contained2"
			Contained contained1 = session.load( Contained.class, 1L );
			contained1.getStringList().add( "foobar" );

			tx.commit();
		}

		try ( Session session = getSessionFactory().openSession() ) {
			Transaction tx = session.beginTransaction();
			List<Containing> results = findResults( session, "contained.stringList", "foobar" );
			assertEquals( 1, results.size() );
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	private List<Containing> findResults(Session session, String fieldName, Object value) {
		FullTextSession fullTextSession = Search.getFullTextSession( session );
		QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder()
				.forEntity( Containing.class ).get();
		Query query = queryBuilder.keyword().onField( fieldName ).matching( value ).createQuery();
		return fullTextSession.createFullTextQuery( query, Containing.class ).list();
	}

	@Entity
	@Indexed
	@Table(name = "Containing")
	public static class Containing {
		@Id
		private Long id;

		@IndexedEmbedded
		@OneToMany(mappedBy = "containing", fetch = FetchType.LAZY) // This property being lazy is essential to this test
		private List<Contained> contained = new ArrayList<>();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public List<Contained> getContained() {
			return contained;
		}

		@Override
		public String toString() {
			return Contained.class.getSimpleName() + "[id=" + id + "]";
		}
	}

	@Entity
	@Table(name = "Contained")
	public static class Contained {
		@Id
		private Long id;

		@ManyToOne
		@ContainedIn
		private Containing containing;

		@ElementCollection
		@CollectionTable(
				name = "site_notes",
				joinColumns = @JoinColumn(name = "site_id", foreignKey = @ForeignKey(name = "fk_site_notes__01"))
		)
		@org.hibernate.annotations.ForeignKey(name = "none") // https://stackoverflow.com/questions/41729709/how-do-i-disable-hibernate-foreign-key-constraint-on-a-bidirectional-association
		@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
		@FieldBridge(impl = BuiltinIterableBridge.class)
		private List<String> stringList = new ArrayList<>();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Containing getContaining() {
			return containing;
		}

		public void setContaining(Containing containing) {
			this.containing = containing;
		}

		public List<String> getStringList() {
			return stringList;
		}

		@Override
		public String toString() {
			return Contained.class.getSimpleName() + "[id=" + id + "]";
		}
	}


}
