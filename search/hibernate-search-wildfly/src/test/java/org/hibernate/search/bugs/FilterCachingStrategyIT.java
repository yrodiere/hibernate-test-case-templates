/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.inject.Inject;

import org.junit.Test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class FilterCachingStrategyIT extends SearchITBase {

	@Deployment
	public static Archive<?> createTestArchive() {
		return createTestArchive(
				MethodHandles.lookup(),
				properties ->
						properties.createProperty().name( "hibernate.search.filter.cache_strategy" )
								.value( CustomFilterCachingStrategy.class.getName() ).up(),
				YourController.class, YourAnnotatedEntity.class,
				NameFilterFactory.class, CustomFilterCachingStrategy.class
		);
	}

	@Inject
	private YourController controller;

	@Test
	public void testFilterCaching() {
		YourAnnotatedEntity yourEntity1 = new YourAnnotatedEntity( 1L, "example" );
		YourAnnotatedEntity yourEntity2 = new YourAnnotatedEntity( 2L, "test" );
		controller.create( yourEntity1, yourEntity2 );

		CustomFilterCachingStrategy.Statistics stats = CustomFilterCachingStrategy.getStatistics();
		assertEquals( 0, stats.getHits() );
		assertEquals( 0, stats.getMisses() );

		List<YourAnnotatedEntity> result = controller.searchUsingPreDefinedFilter( "example" );
		assertEquals( 1, result.size() );
		assertEquals( 1L, (long) result.get( 0 ).getId() );
		assertEquals( 0, stats.getHits() );
		assertEquals( 1, stats.getMisses() );

		result = controller.searchUsingPreDefinedFilter( "test" );
		assertEquals( 1, result.size() );
		assertEquals( 2L, (long) result.get( 0 ).getId() );
		assertEquals( 0, stats.getHits() );
		assertEquals( 2, stats.getMisses() );

		result = controller.searchUsingPreDefinedFilter( "example" );
		assertEquals( 1, result.size() );
		assertEquals( 1L, (long) result.get( 0 ).getId() );
		assertEquals( 1, stats.getHits() );
		assertEquals( 2, stats.getMisses() );
	}

}
