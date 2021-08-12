package org.hibernate.search.bugs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
public class Depot implements IArchivable {

	@Id
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bureau_hypotheque_fk")
	protected BureauHypotheque bureauHypotheque;

	public void setBureauHypotheque(BureauHypotheque bureauHypotheque) {
		this.bureauHypotheque = bureauHypotheque;
	}

	@IndexedEmbedded(includePaths = { "nomComplet", "id"})
	public BureauHypotheque getBureauHypotheque() {
		return this.bureauHypotheque;
	}
}