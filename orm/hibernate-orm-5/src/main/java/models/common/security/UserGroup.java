package models.common.security;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import models.common.DatabaseEntity;

@Entity
public class UserGroup extends DatabaseEntity {

	private Set<JafSid> members = new LinkedHashSet<>();

	@ManyToMany
	public Set<JafSid> getMembers() {
		return members;
	}

	public void setMembers(Set<JafSid> members) {
		this.members = members;
	}
}
