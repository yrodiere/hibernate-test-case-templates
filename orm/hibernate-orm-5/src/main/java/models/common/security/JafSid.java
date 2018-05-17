package models.common.security;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import models.common.Base;

@Entity
public class JafSid extends Base {

	private Set<UserGroup> groups = new LinkedHashSet<>();
	private SidEntity relatedEntity;
	private String sid;

	@ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public SidEntity getRelatedEntity() {
		return relatedEntity;
	}

	public void setRelatedEntity(SidEntity relatedEntity) {
		this.relatedEntity = relatedEntity;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}
