package org.hibernate.bugs;

import org.hibernate.cfg.AvailableSettings;
import org.junit.Test;

import models.common.File;
import models.common.security.Authority;
import models.common.security.JafSid;
import models.common.security.UserGroup;

public class TestCase extends AbstractTestCase {
	public TestCase() {
		super(JafSid.class, UserGroup.class);
		configure(c -> c.setProperty(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "30"));
	}

	@Test
	public void hhh12594() {
		System.out.println("Crashes during bootstrapping.");
	}

}
