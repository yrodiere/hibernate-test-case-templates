/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;
import javax.annotation.Resource;

import org.hibernate.search.bugs.util.VersionTestHelper;
import org.hibernate.search.engine.Version;

import org.junit.Before;
import org.junit.runner.RunWith;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence21.PersistenceDescriptor;
import org.jboss.shrinkwrap.descriptor.api.persistence21.PersistenceUnit;
import org.jboss.shrinkwrap.descriptor.api.persistence21.Properties;

@RunWith(Arquillian.class)
public abstract class SearchITBase {

	private static final String EXPECTED_SEARCH_VERSION_RESOURCE = "expectedHibernateSearchVersion";

	protected static Archive<?> createTestArchive(MethodHandles.Lookup creationContext,
			Consumer<Properties<?>> propertyContributor, Class<?> ... classesUsedInTest) {
		WebArchive archive = ShrinkWrap
				.create( WebArchive.class, creationContext.lookupClass().getSimpleName() + ".war" )
				.addClasses( SearchITBase.class, VersionTestHelper.class )
				.addClasses( classesUsedInTest )
				.addAsResource( persistenceXml( propertyContributor ), "META-INF/persistence.xml" )
				.addAsWebInfResource( EmptyAsset.INSTANCE, "beans.xml" )
				.addAsWebInfResource( webXml(), "web.xml" );;
		return archive;
	}

	private static Asset webXml() {
		String webXml = Descriptors.create( org.jboss.shrinkwrap.descriptor.api.webapp31.WebAppDescriptor.class )
				.createEnvEntry()
				.envEntryName( EXPECTED_SEARCH_VERSION_RESOURCE )
				.envEntryValue(
						"main".equals( VersionTestHelper.getModuleSlotString() ) ?
								VersionTestHelper.getDependencyVersionHibernateSearchBuiltIn() :
								VersionTestHelper.getDependencyVersionHibernateSearch() )
				.envEntryType( "java.lang.String" )
				.up()
				.exportAsString();
		return new StringAsset( webXml );
	}

	private static Asset persistenceXml(Consumer<Properties<?>> propertyContributor) {
		Properties<PersistenceUnit<PersistenceDescriptor>> currentState = Descriptors.create( PersistenceDescriptor.class )
				.version( "2.0" )
				.createPersistenceUnit()
				.name( "primary" )
				.jtaDataSource( "java:jboss/datasources/ExampleDS" )
				.getOrCreateProperties()
				.createProperty().name( "hibernate.hbm2ddl.auto" ).value( "create-drop" ).up()
				.createProperty().name( "hibernate.search.default.lucene_version" ).value( "LUCENE_CURRENT" ).up()
				// Use the deprecated name on purpose, to be able to run with Hibernate Search 5.5
				.createProperty().name( "hibernate.search.default.directory_provider" ).value( "ram" ).up()
				.createProperty().name( "wildfly.jpa.hibernate.search.module" )
						.value( VersionTestHelper.getHibernateSearchModuleIdentifier() ).up()
				.createProperty().name( "jboss.as.jpa.providerModule" )
						.value( VersionTestHelper.getHibernateORMModuleIdentifier() ).up();

		propertyContributor.accept( currentState );

		String persistenceXml = currentState.up().up()
				.exportAsString();
		return new StringAsset( persistenceXml );
	}

	@Resource(name = EXPECTED_SEARCH_VERSION_RESOURCE)
	private String expectedSearchVersion;

	@Before
	public void checkHibernateSearchVersion() throws Exception {
		assertEquals(
				"The version of Hibernate Search used in your test did not match the expected version."
				+ " There is probably something wrong with your setup.",
				expectedSearchVersion, Version.getVersionString()
		);
	}
}
