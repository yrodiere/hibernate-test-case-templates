package org.hibernate.search.bugs;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Entity
public class BureauHypotheque {

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

	@OneToMany(mappedBy = "bureauHypotheque")
	protected transient Set<Depot> actesPrives = new HashSet<>();

	public Set<Depot> getActesPrives() {
		return actesPrives;
	}

	public void setActesPrives(Set<Depot> actesPrives) {
		this.actesPrives = actesPrives;
	}

}
