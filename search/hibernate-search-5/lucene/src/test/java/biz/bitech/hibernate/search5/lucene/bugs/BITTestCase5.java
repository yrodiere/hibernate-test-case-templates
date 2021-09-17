package biz.bitech.hibernate.search5.lucene.bugs;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class BITTestCase5 {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void setup() {
		entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
	}

	@After
	public void teardown() {
		if ( entityManagerFactory != null ) {
			entityManagerFactory.close();
			entityManagerFactory = null;
		}
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-NNNNN") // Please fill in the JIRA key of your issue
	@SuppressWarnings("unchecked")
	public void testYourBug() {
		EntityManager em = entityManagerFactory.createEntityManager();

		{
			EntityTransaction userTransaction = em.getTransaction();
			userTransaction.begin();

			Vendor vendor1 = new Vendor(1L, "Motor Company");
			Item item1 = new Item(1L, "Electric Motor");

			//User adds an item
			em.persist(item1);

			//User adds an vendor
			em.persist(vendor1);

			userTransaction.commit();
		}
		{
			// later, an item cost is added, which should reindex the item
			EntityTransaction userTransaction = em.getTransaction();
			userTransaction.begin();

			Vendor vendor1 = new Vendor(1L, "Motor Company");
			Item item1 = new Item(1L, "Electric Motor");

			ItemVendorInfo itemVendorInfo1 = new ItemVendorInfo(1L, item1, vendor1, new BigDecimal("1000"));
			em.persist(itemVendorInfo1);

			// if this isn't done, the index will not see any of the elements that belong in the @OneToMany collection
			em.flush();
			em.refresh(itemVendorInfo1);
			em.refresh(itemVendorInfo1.getItem());

			userTransaction.commit();
		}

		{
			// test searching items by vendor, to prove indexing of
			// @OneToMany @IndexedEmbedded vendorInfos was successful after persist
			Session s = (Session) em.getDelegate();

			FullTextSession session = Search.getFullTextSession(s);
			QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity(Item.class).get();
			Query query = qb.keyword().onField("vendorInfos.vendor.id").matching("1").createQuery();

			List<Item> result = (List<Item>) session.createFullTextQuery(query).list();
			assertEquals(1, result.size());
			assertEquals(1l, (long) result.get(0).getId());
		}
	}

}
