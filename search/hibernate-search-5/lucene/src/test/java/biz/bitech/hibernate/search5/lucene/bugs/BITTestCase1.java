package biz.bitech.hibernate.search5.lucene.bugs;

import java.math.BigDecimal;
import java.util.List;

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

import static org.junit.Assert.*;

public class BITTestCase1 {

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
			ItemVendorInfo itemVendorInfo1 = new ItemVendorInfo(1L, item1, vendor1, new BigDecimal("1000"));

			//User adds an item
			em.persist(item1);

			//User adds an vendor
			em.persist(vendor1);

			//User adds an item cost to purchase from vendor 1
			em.persist(itemVendorInfo1);

			// we would prefer this not to be the case, but
			// at this point, vendorInfos is still null after persist
			assertNull(item1.getVendorInfos());

			// these two lines prepares item1 for indexing
			// by adding a proxy to vendorInfos that indexing will use
			// to traverse the @OneToMany
			em.flush();
			em.refresh(item1);

			// vendorInfos is now alive
			assertNotNull(item1.getVendorInfos());

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

		{
			EntityTransaction userTransaction = em.getTransaction();
			userTransaction.begin();

			//user updates description of item1 by sending back a detached object
			Item item1 = new Item(1L, "5HP Electric Motor");  // simulate detached item with updated property

			item1 = em.merge(item1);

			// we would prefer this not to be the case, but
			// at this point, vendorInfos is still null
			assertNull(item1.getVendorInfos());

			// refresh item1 to add proxy to vendorInfos
			// that indexing will use to traverse the @OneToMany
			em.refresh(item1);

			// vendorInfos is now alive
			assertNotNull(item1.getVendorInfos());

			userTransaction.commit();
		}

		// test searching items by vendor, to prove indexing of
		// @OneToMany @IndexedEmbedded vendorInfos was successful after merge
		{
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
