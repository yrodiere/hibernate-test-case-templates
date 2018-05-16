package com.enhancement.generics;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class Generic extends AbstractGeneric<Generic.Type> {
	public enum Type implements Marker {
		ONE
	}

}