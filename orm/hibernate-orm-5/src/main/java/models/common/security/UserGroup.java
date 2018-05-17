package models.common.security;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
public class UserGroup extends SidEntity {

	private Set<Authority> authorities = new LinkedHashSet<>();
	private Set<JafSid> members = new LinkedHashSet<>();

	@ManyToMany(targetEntity = Authority.class, fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<JafSid> getMembers() {
		return members;
	}

	public void setMembers(Set<JafSid> members) {
		this.members = members;
	}
}
