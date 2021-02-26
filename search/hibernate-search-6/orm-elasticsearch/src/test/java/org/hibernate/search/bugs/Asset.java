package org.hibernate.search.bugs;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Indexed
public class Asset extends SearchResult {
    public static final int ASSET_RESULT_TYPE_SORT = Series.SERIES_RESULT_TYPE_SORT + 1; // TODO delete?

    @Id
    @DocumentId // TODO delete?
	private Long id;

    @FullTextField(analyzer = "customAnalyzer")
    private String title;

    @IndexedEmbedded(structure = ObjectStructure.NESTED)
    @OneToMany(mappedBy="asset", cascade=CascadeType.ALL)
    private Set<DataPackage> dataPackages;

    @IndexedEmbedded(includeDepth = 1)
    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

	public Asset() {}

    public Asset(Long id, String title, Set<DataPackage> dataPackages, Series series) {
        this.id = id;
        this.title = title;
        this.dataPackages = dataPackages;
        this.series = series;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<DataPackage> getDataPackages() {
        return dataPackages;
    }

    public void setDataPackages(Set<DataPackage> dataPackages) {
        this.dataPackages = dataPackages;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    @Override
    public int getResultTypeSort() { // TODO delete?
        return ASSET_RESULT_TYPE_SORT;
    }
}
