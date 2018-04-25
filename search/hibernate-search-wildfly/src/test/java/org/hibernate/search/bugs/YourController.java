/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import org.apache.lucene.search.Query;

@Transactional
public class YourController {

	@PersistenceContext
	private EntityManager em;

	public void create(YourAnnotatedEntity ... entities) {
		for ( YourAnnotatedEntity entity : entities ) {
			em.persist( entity );
		}
	}

	public List<YourAnnotatedEntity> search(String terms) {
		FullTextEntityManager ftEm = Search.getFullTextEntityManager( em );
		QueryBuilder qb = ftEm.getSearchFactory().buildQueryBuilder().forEntity( YourAnnotatedEntity.class ).get();
		Query query = qb.keyword().onField( "name" ).matching( terms ).createQuery();
		return (List<YourAnnotatedEntity>) ftEm.createFullTextQuery( query ).getResultList();
	}
}
