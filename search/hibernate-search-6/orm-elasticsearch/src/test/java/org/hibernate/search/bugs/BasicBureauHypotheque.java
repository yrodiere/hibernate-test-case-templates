package org.hibernate.search.bugs;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@MappedSuperclass
public class BasicBureauHypotheque {

	@Id
	@GenericField
	private Long id;

	@FullTextField
	private String nomComplet;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomComplet() {
		return nomComplet;
	}

	public void setNomComplet(String nomComplet) {
		this.nomComplet = nomComplet;
	}
}
