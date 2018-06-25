package models.common.security;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import models.common.DatabaseEntity;

@Entity
public class JafSid extends DatabaseEntity {

	private Set<UserGroup> groups = new LinkedHashSet<>();

	@ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
	public Set<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}
}
