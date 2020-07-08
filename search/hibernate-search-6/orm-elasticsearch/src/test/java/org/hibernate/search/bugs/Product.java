package org.hibernate.search.bugs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.engine.backend.document.model.dsl.ObjectFieldStorage;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Indexed
public class Product {

	@Id
	@DocumentId
	private Long id;

	@FullTextField(analyzer = "nameAnalyzer")
	private String name;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
    @Fetch(value = FetchMode.SUBSELECT)
    @IndexedEmbedded(storage = ObjectFieldStorage.NESTED)
    private List<ProductMetadata> productMetadata = new ArrayList<>();

	protected Product() {
	}

	public Product(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public List<ProductMetadata> getProductMetadata() {
        return productMetadata;
    }

    public void setProductMetadata(List<ProductMetadata> productMetadata) {
        this.productMetadata = productMetadata;
    }

}
