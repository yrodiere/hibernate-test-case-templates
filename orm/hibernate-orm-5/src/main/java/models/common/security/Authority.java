package models.common.security;

import javax.persistence.Entity;

@Entity
public class Authority extends SidEntity {
	private String authority;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
