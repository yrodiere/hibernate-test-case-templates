package models.specific;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import models.common.Base;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CacheOnJoinedInheritance extends Base {

	@Id
	@GeneratedValue
	public int id;

}