package models.specific;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import models.common.File;

@Entity
public class PropertyAccessCollectionEntity {

	private Set<File> attachments;
	private int id;

	@OneToMany
	public Set<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<File> attachments) {
		this.attachments = attachments;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
