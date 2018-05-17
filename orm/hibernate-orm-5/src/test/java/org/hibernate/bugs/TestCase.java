package org.hibernate.bugs;

import org.hibernate.engine.spi.ExtendedSelfDirtinessTracker;
import org.junit.Assert;
import org.junit.Test;

import models.common.File;
import models.specific.DefaultFieldAccessCollectionEntity;
import models.specific.DefaultPropertyAccessButFieldAccessCollectionEntity;
import models.specific.PropertyAccessCollectionEntity;

public class TestCase extends AbstractTestCase {
	public TestCase() {
		super(DefaultFieldAccessCollectionEntity.class, DefaultPropertyAccessButFieldAccessCollectionEntity.class, PropertyAccessCollectionEntity.class,
				File.class);
	}

	@Test
	public void hhh12593_1_DefaultPropertyAccessButFieldAccessCollectionEntity() {
		assertExtended(new DefaultPropertyAccessButFieldAccessCollectionEntity());

	}

	@Test
	public void hhh12593_2_PropertyAccessCollectionEntity() {
		assertExtended(new PropertyAccessCollectionEntity());

	}

	@Test
	public void hhh12593_3_DefaultFieldAccessCollectionEntity() {
		assertExtended(new DefaultFieldAccessCollectionEntity());
	}

	private void assertExtended(Object entity) {
		Assert.assertTrue(entity + " should be instance of " + ExtendedSelfDirtinessTracker.class.getSimpleName() + ".",
				entity instanceof ExtendedSelfDirtinessTracker);
	}
}
