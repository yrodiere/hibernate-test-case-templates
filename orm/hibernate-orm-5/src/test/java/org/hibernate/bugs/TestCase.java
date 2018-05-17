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
	public void hhh12425() {
		doInOpenTransaction((s, tx) -> {
			Authority a = new Authority();
			JafSid sid = new JafSid();
			sid.setRelatedEntity(a);
			a.setSid(sid);
			s.save(a);
			s.flush();
			s.clear();
			//throws exception in next line!
			s.get(Authority.class, a.getId());
		});
	}

}
