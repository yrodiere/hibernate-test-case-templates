package biz.bitech.hibernate.search6.lucene.bugs;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

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

public class BITTestCase6 {

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
