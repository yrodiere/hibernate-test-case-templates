package models.common.security;

import javax.persistence.Entity;

@Entity
public class User extends SidEntity {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}