package org.hibernate.search.bugs;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
public class Info {

	@Id
	@Column(name = "Info_ID")
	private Long id;

	@OneToOne
	private Root root;

	@IndexedEmbedded
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Info_2_User_Data",
			joinColumns = {
					@JoinColumn(name = "Info_ID", referencedColumnName = "Info_ID", columnDefinition = "INT(7)")
			},
			inverseJoinColumns = {
					@JoinColumn(name = "User_ID", referencedColumnName = "User_ID", columnDefinition = "INT(7)")
			})
	private Set<User> userList = new HashSet<>();

	@IndexedEmbedded
	@ManyToOne
	@JoinColumn(name = "Info_MainUser", referencedColumnName = "User_ID", columnDefinition = "INT(7)")
	private User mainUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Root getRoot() {
		return root;
	}

	public void setRoot(Root root) {
		this.root = root;
	}

	public Set<User> getUserList() {
		return userList;
	}

	public void setUserList(Set<User> userList) {
		this.userList = userList;
	}

	public User getMainUser() {
		return mainUser;
	}

	public void setMainUser(User mainUser) {
		this.mainUser = mainUser;
	}
}
