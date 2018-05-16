package com.enhancement.secondlevelcache;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class NoCacheConcurrencyStrategyEntity {

	@Id
	@GeneratedValue
	public int id;
}