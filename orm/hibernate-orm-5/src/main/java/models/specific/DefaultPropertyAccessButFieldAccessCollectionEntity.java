package models.specific;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import models.common.File;

@Entity
public class DefaultPropertyAccessButFieldAccessCollectionEntity {

	private int id;
	@Access(AccessType.FIELD)
	@OneToMany
	private Set<File> attachments;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
