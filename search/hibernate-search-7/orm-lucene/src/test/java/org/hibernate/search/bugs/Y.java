package org.hibernate.search.bugs;

import org.hibernate.annotations.BatchSize;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Y")
@BatchSize(size = 500)
@Indexed
public class Y
{
	@Id
	private Long id;

	@FullTextField
	private String text;

	@OneToOne()
	@JoinColumn(name = "X_ID")
	private X x;

	protected Y() {
	}

	public Y(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public X getX() {
		return x;
	}

	public void setX(X x) {
		this.x = x;
	}
}