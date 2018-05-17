package org.hibernate.bugs;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.junit.Assert;
import org.junit.Test;

import models.common.File;
import models.specific.CacheOnJoinedInheritance;

public class TestCase extends AbstractTestCase {
	public TestCase() {
		super(CacheOnJoinedInheritance.class, File.class);
		configure(c -> c.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, TRUE));
	}

	@Test
	public void hhh12588() {
		MetamodelImplementor m = sessionFactory().getMetamodel();
		Class<?> c = CacheOnJoinedInheritance.class;
		Assert.assertTrue(c.getSimpleName() + " cannot read from cache!", m.entityPersister(c).canReadFromCache());
		Assert.assertTrue(c.getSimpleName() + " cannot write to cache!", m.entityPersister(c).canWriteToCache());

	}
}
