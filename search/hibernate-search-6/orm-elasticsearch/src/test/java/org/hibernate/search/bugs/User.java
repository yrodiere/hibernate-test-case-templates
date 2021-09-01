package org.hibernate.search.bugs;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Entity
public class User {

	@Id
	@Column(name = "User_ID")
	private Long id;

	@FullTextField(name = "user_name")
	@Column(name = "User_Name", length = 20)
	private String name;

	@ManyToMany(mappedBy = "userList")
	private Set<Info> infoForUserList = new HashSet<>();

	@OneToMany(mappedBy = "mainUser")
	private Set<Info> infoForMainUser = new HashSet<>();

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

	public Set<Info> getInfoForUserList() {
		return infoForUserList;
	}

	public void setInfoForUserList(Set<Info> infoForUserList) {
		this.infoForUserList = infoForUserList;
	}

	public Set<Info> getInfoForMainUser() {
		return infoForMainUser;
	}

	public void setInfoForMainUser(Set<Info> infoForMainUser) {
		this.infoForMainUser = infoForMainUser;
	}
}
