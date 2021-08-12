package org.hibernate.search.bugs;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class BureauHypotheque extends BasicBureauHypotheque {

	@OneToMany(mappedBy = "bureauHypotheque")
	protected transient Set<Depot> actesPrives = new HashSet<>();

	public Set<Depot> getActesPrives() {
		return actesPrives;
	}

	public void setActesPrives(Set<Depot> actesPrives) {
		this.actesPrives = actesPrives;
	}

}
