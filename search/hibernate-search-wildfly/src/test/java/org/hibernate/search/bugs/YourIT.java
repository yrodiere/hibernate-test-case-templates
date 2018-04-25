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

public class YourIT extends SearchITBase {

	@Deployment
	public static Archive<?> createTestArchive() {
		return createTestArchive(
				MethodHandles.lookup(),
				properties -> {},
				YourController.class, YourAnnotatedEntity.class
		);
	}

	@Inject
	private YourController controller;

	@Test
	public void testYourBug() {
		YourAnnotatedEntity yourEntity1 = new YourAnnotatedEntity( 1L, "example" );
		YourAnnotatedEntity yourEntity2 = new YourAnnotatedEntity( 2L, "test" );
		controller.create( yourEntity1, yourEntity2 );

		List<YourAnnotatedEntity> result = controller.search( "example" );
		assertEquals( 1, result.size() );
		assertEquals( 1L, (long) result.get( 0 ).getId() );
	}

}
