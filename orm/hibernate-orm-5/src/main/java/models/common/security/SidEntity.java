package models.common.security;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import models.common.Base;

@Entity
public class SidEntity extends Base {

	private JafSid sid;

	@OneToOne(mappedBy = "relatedEntity", optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	public JafSid getSid() {
		return sid;
	}

	public void setSid(JafSid sid) {
		this.sid = sid;
	}
}
