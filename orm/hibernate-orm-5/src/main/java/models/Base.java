package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

@Entity
@Polymorphism(type = PolymorphismType.EXPLICIT)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Base extends DatabaseEntity {

	private int id;
	private Set<File> files;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	public Set<File> getFiles() {
		return files;
	}

	public void setFiles(Set<File> files) {
		this.files = files;
	}
}
