package org.hibernate.search.bugs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Indexed
public class Depot extends BasicDepot implements IArchivable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bureau_hypotheque_fk")
	protected BureauHypotheque bureauHypotheque;

	@IndexedEmbedded(includePaths = { "nomComplet", "id"})
	public BureauHypotheque getBureauHypotheque() {
		return this.bureauHypotheque;
	}
}