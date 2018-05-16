package com.genericsenhancement;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@MappedSuperclass
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class AbstractMail {

	@Access(AccessType.PROPERTY)
	private Set<String> to = new LinkedHashSet<>();

	@Id
	@GeneratedValue
	public int id;

	@ElementCollection
	public Set<String> getTo() {
		return to;
	}

	private void setTo(Set<String> to) {
		this.to = to;
	}
}