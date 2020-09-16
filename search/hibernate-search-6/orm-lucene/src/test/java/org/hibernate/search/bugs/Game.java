package org.hibernate.search.bugs;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@Indexed
public class Game extends GameInfo {

	@Id
	@DocumentId
	private Long id;

	protected Game() {
	}

	public Game(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
