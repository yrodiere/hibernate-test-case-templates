package models.specific;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@MappedSuperclass
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class AbstractGeneric<T extends Marker> {

	@Id
	@GeneratedValue
	public int id;

	@Access(AccessType.PROPERTY)
	private T entity;

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}