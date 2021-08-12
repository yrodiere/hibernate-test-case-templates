package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{ YourAnnotatedEntity.class, BasicDepot.class, Depot.class, BasicBureauHypotheque.class, BureauHypotheque.class };
	}

	@Test
	public void testYourBug() {
		try ( Session s = getSessionFactory().openSession() ) {
			s.beginTransaction();
			BureauHypotheque bureauHypotheque = new BureauHypotheque();
			bureauHypotheque.setId( 1L );
			bureauHypotheque.setNomComplet( "foo" );

			Depot depot = new Depot();
			depot.setId( 2L );

			depot.setBureauHypotheque( bureauHypotheque );
			bureauHypotheque.getActesPrives().add( depot );

			s.persist( depot );
			s.persist( bureauHypotheque );
			s.getTransaction().commit();
		}

		try ( Session s = getSessionFactory().openSession() ) {
			s.beginTransaction();
			Depot depot = s.load( Depot.class, 2L );
			BureauHypotheque bureauHypotheque = depot.getBureauHypotheque();
			assertThat( bureauHypotheque ).isNotNull();
			Set<Depot> actesPrives = bureauHypotheque.getActesPrives();
			assertThat( actesPrives ).isNotEmpty();
			s.getTransaction().commit();
		}
	}

}
