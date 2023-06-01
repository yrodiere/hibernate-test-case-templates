package org.hibernate.search.bugs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Indexed
public class A {

	@Id
	private Long id;

	@OneToMany(mappedBy = "a")
	@IndexedEmbedded(structure = ObjectStructure.NESTED, includePaths = {"testId", "cList.type"})
	private List<B> bList = new ArrayList<>();

	protected A() {
	}

	public A(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<B> getBList() {
		return bList;
	}

	public void setBList(List<B> bList) {
		this.bList = bList;
	}

}
