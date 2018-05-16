package com.enhancement.secondlevelcache;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

@Entity
@Polymorphism(type = PolymorphismType.EXPLICIT)
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseEntity {
	@Id
	@GeneratedValue
	public int id;
}
