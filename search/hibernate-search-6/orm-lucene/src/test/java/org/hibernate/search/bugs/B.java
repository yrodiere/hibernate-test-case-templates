package org.hibernate.search.bugs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;

@Entity
@Indexed
public class B {

	@Id
	@GeneratedValue
	private Long id;

	@ScaledNumberField(decimalScale = 0)
	private BigInteger testId;

	@ManyToOne
	private A a;

	@OneToMany(mappedBy = "b")
	@IndexedEmbedded(structure = ObjectStructure.NESTED, includePaths = {"type"})
	private List<C> cList = new ArrayList<>();

	protected B() {
	}

	public B(A a, int testId) {
		this.testId = BigInteger.valueOf( testId );
		this.a = a;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getTestId() {
		return testId;
	}

	public void setTestId(BigInteger testId) {
		this.testId = testId;
	}

	public A getA() {
		return a;
	}

	public void setA(A a) {
		this.a = a;
	}

	public List<C> getCList() {
		return cList;
	}

	public void setCList(List<C> cList) {
		this.cList = cList;
	}
}
