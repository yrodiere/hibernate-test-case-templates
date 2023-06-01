package org.hibernate.search.bugs;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;

@Entity
@Indexed
public class C {

	@Id
	@GeneratedValue
	private Long id;

	@ScaledNumberField(decimalScale = 0, sortable = Sortable.YES)
	private BigInteger type;

	@ManyToOne
	private B b;

	protected C() {
	}

	public C(B b, int type) {
		this.type = BigInteger.valueOf( type );
		this.b = b;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getType() {
		return type;
	}

	public void setType(BigInteger type) {
		this.type = type;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

}
