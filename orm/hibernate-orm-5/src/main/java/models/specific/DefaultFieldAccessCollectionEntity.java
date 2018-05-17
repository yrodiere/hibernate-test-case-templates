package models.specific;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import models.common.File;

@Entity
public class DefaultFieldAccessCollectionEntity {

	@Id
	private int id;

	@OneToMany
	private Set<File> attachments;
}
