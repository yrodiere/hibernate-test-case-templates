package org.hibernate.bugs;

import org.hibernate.cfg.AvailableSettings;
import org.junit.Test;

import models.specific.NoCacheConcurrencyStrategyEntity;

public class TestCase extends AbstractTestCase {
	public TestCase() {
		super(NoCacheConcurrencyStrategyEntity.class);
		configure(c -> c.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, TRUE));
	}

	@Test
	public void hhh12587() {
		doInOpenTransaction((s, tx) -> {
			NoCacheConcurrencyStrategyEntity e = new NoCacheConcurrencyStrategyEntity();
			s.save(e);
			s.flush();
			s.clear();
		});
	}
}
