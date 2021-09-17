package biz.bitech.hibernate.search6.lucene.bugs;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BITTestCase4 {

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
			// ****************************
			//  new for Hibernate Search 6
			// ****************************
			//
			// this would work, but hard coding is not desired
			// item1.setVendorInfos(new HashSet<>());
			// item1.getVendorInfos().add(itemVendorInfo1);
			//
			// objects come in pre-populated from the client, and
			// the server just needs to validate and save
			em.persist(itemVendorInfo1);
			em.flush();
			em.refresh(item1);
			SearchSession searchSession = Search.session(em);
			SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
			indexingPlan.addOrUpdate( item1 );
			
			userTransaction.commit();
		}

		{
			SearchSession searchSession = Search.session(em);

			List<Item> hits = searchSession.search( Item.class )
					.where( f -> f.match().field( "vendorInfos.vendor.id" ).matching( 1L ) )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 1 )
					.element( 0 ).extracting( Item::getId )
					.isEqualTo( 1L );
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

			// do not flush, or will cause next search to fail
			// em.flush();

			// refresh item1 to add proxy to vendorInfos
			// that indexing will use to traverse the @OneToMany
			em.refresh(item1);

			// vendorInfos is now alive
			assertNotNull(item1.getVendorInfos());

			userTransaction.commit();
		}

		{
			SearchSession searchSession = Search.session(em);

			List<Item> hits = searchSession.search( Item.class )
					.where( f -> f.match().field( "vendorInfos.vendor.id" ).matching( 1L ) )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 1 )
					.element( 0 ).extracting( Item::getId )
					.isEqualTo( 1L );
		}
	}

}
