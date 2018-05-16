package com.collectionwithblob;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bar {
	@OneToMany(mappedBy = "bar", fetch = FetchType.LAZY)
	public Set<Foo> foos = new HashSet<>();

	@Id
	@GeneratedValue
	public int id;
}