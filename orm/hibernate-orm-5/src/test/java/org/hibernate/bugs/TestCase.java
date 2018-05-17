package org.hibernate.bugs;

import org.junit.Test;

import models.common.File;
import models.common.security.Authority;
import models.common.security.JafSid;
import models.common.security.UserGroup;

public class TestCase extends AbstractTestCase {
	public TestCase() {
		super(Authority.class, JafSid.class, UserGroup.class, File.class);
	}

	@Test
	public void hhhxxx() {
		doInOpenTransaction((s, tx) -> {

		});
	}

}
