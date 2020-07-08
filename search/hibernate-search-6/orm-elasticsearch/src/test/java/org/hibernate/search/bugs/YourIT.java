package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ Product.class , ProductMetadata.class};
	}

    @Test
    public void testYourBug() {
        try (Session s = getSessionFactory().openSession()) {
            final Product product1 = new Product(1L, "Jane Smith");

            final ProductMetadata pm11 = new ProductMetadata();
            pm11.setKey("genre");
            pm11.setValue("crime");
            pm11.setProduct(product1);

            final ProductMetadata pm12 = new ProductMetadata();
            pm12.setKey("author");
            pm12.setValue("Christie");
            pm12.setProduct(product1);

            final Product product2 = new Product(2L, "John Doe");

            final ProductMetadata pm21 = new ProductMetadata();
            pm21.setKey("genre");
            pm21.setValue("spy");
            pm21.setProduct(product2);

            final ProductMetadata pm22 = new ProductMetadata();
            pm22.setKey("author");
            pm22.setValue("Forsyth");
            pm22.setProduct(product2);

            final Transaction tx = s.beginTransaction();
            s.persist(product1);
            s.persist(pm11);
            s.persist(pm12);

            s.persist(product2);

            s.persist(pm21);

            s.persist(pm22);
            tx.commit();
        }
        try (Session s = getSessionFactory().openSession()) {
            final Product productFromDB = s.get(Product.class, 1L);
            assertEquals(2, productFromDB.getProductMetadata().size());// The metadata is linked to
                                                                       // the product
        }

        try (final Session session = getSessionFactory().openSession()) {
            final SearchSession searchSession = Search.session(session);

            final List<Product> hits = searchSession.search(Product.class).where(f -> f.bool(b -> {
                b.must(subPredicate -> subPredicate.nested()
                    .objectField("productMetadata")
                    .nest(subPredicate.bool()
                        .filter(
                            subPredicate.match().field("productMetadata.key").matching("genre"))
                        .must(subPredicate.match()
                            .field("productMetadata.value")
                            .matching("crime"))));
            })).fetchHits(20);

            assertEquals(1, hits.size());
            assertEquals((Long) 1L, hits.get(0).getId());
        }
    }

    @Test
    public void testYourWorkaround() {
        try (Session s = getSessionFactory().openSession()) {
            final Product product1 = new Product(1L, "Jane Smith");

            final ProductMetadata pm11 = new ProductMetadata();
            pm11.setKey("genre");
            pm11.setValue("crime");
            pm11.setProduct(product1);

            final ProductMetadata pm12 = new ProductMetadata();
            pm12.setKey("author");
            pm12.setValue("Christie");
            pm12.setProduct(product1);

            final Product product2 = new Product(2L, "John Doe");

            final ProductMetadata pm21 = new ProductMetadata();
            pm21.setKey("genre");
            pm21.setValue("spy");
            pm21.setProduct(product2);

            final ProductMetadata pm22 = new ProductMetadata();
            pm22.setKey("author");
            pm22.setValue("Forsyth");
            pm22.setProduct(product2);

            final Transaction tx = s.beginTransaction();
            s.persist(product1);
            s.persist(pm11);
            s.persist(pm12);

            s.persist(product2);

            s.persist(pm21);

            s.persist(pm22);
            tx.commit();
        }

        try (Session s = getSessionFactory().openSession()) {
            final Product productFromDB = s.get(Product.class, 1L);
            assertEquals(2, productFromDB.getProductMetadata().size());// The metadata is linked to
                                                                       // the product
        }

        try (final Session session = getSessionFactory().openSession()) {
            final SearchSession searchSession = Search.session(session);

            final List<Product> hits = searchSession.search(Product.class).where(f -> f.bool(b -> {
                b.must(subPredicate -> subPredicate.nested()
                    .objectField("productMetadata")
                    .nest(subPredicate.bool()
                        .filter(
                            subPredicate.match().field("productMetadata.key").matching("genre"))
                        .must(
                            subPredicate.match()
                                .field("productMetadata.value")
                                .matching("crime"))));
            })).fetchHits(20);

            assertEquals(0, hits.size());// This should actually be 1
        }

        try (Session s = getSessionFactory().openSession()) {
            final Product productFromDB = s.get(Product.class, 1L);
            final Transaction tx = s.beginTransaction();
            productFromDB.setName(productFromDB.getName() + "Changed");
            tx.commit();// Now I recommit the product, without changing the metadata
        }
        try (Session s = getSessionFactory().openSession()) {
            final Product productFromDB = s.get(Product.class, 1L);
            assertEquals(2, productFromDB.getProductMetadata().size());// The metadata is linked to
                                                                       // the product
        }

        try (final Session session = getSessionFactory().openSession()) {
            final SearchSession searchSession = Search.session(session);

            final List<Product> hits = searchSession.search(Product.class).where(f -> f.bool(b -> {
                b.must(subPredicate -> subPredicate.nested()
                    .objectField("productMetadata")
                    .nest(subPredicate.bool()
                        .filter(
                            subPredicate.match().field("productMetadata.key").matching("genre"))
                        .must(subPredicate.match()
                            .field("productMetadata.value")
                            .matching("crime"))));
            })).fetchHits(20);

            assertEquals(1, hits.size());
            assertEquals((Long) 1L, hits.get(0).getId());
        }
    }

}
