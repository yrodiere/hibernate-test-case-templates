package org.hibernate.search.bugs;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Indexed
public class Series extends SearchResult {
	public static final int SERIES_RESULT_TYPE_SORT = 0; // TODO delete?

	@Id
	@DocumentId // TODO delete?
	private Long id;

	@FullTextField(analyzer = "customAnalyzer")
	private String name;
	
    @IndexedEmbedded(structure = ObjectStructure.NESTED)
    @OneToMany(mappedBy="series", cascade=CascadeType.ALL)
    private Set<Asset> episodes;

	public Series() {}

	public Series(Long id, String name, Set<Asset> episodes) {
		this.id = id;
		this.name = name;
		this.episodes = episodes;
	}

	public Long getId() {
		return id;
	}

    public void setId(Long id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Asset> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Set<Asset> episodes) {
		this.episodes = episodes;
	}

    @Override
    public int getResultTypeSort() { // TODO delete?
        return SERIES_RESULT_TYPE_SORT;
    }
}
