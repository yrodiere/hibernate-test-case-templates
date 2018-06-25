package org.hibernate.bugs;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

import org.hibernate.cfg.AvailableSettings;

import org.junit.Test;

public class TestCase extends AbstractTestCase {
	public TestCase() {
		super(JafSid.class, UserGroup.class);
		configure(c -> c.setProperty(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "30"));
	}

	@Test
	public void hhh12594() {
		System.out.println("Crashes during bootstrapping.");
	}

	@MappedSuperclass
	public abstract static class DatabaseEntity {
		private int id;

		@Id
		@GeneratedValue
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}

	@Entity
	public static class UserGroup extends DatabaseEntity {

		private Set<JafSid> members = new LinkedHashSet<>();

		@ManyToMany
		public Set<JafSid> getMembers() {
			return members;
		}

		public void setMembers(Set<JafSid> members) {
			this.members = members;
		}
	}

	@Entity
	public static class JafSid extends DatabaseEntity {

		private Set<UserGroup> groups = new LinkedHashSet<>();

		@ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
		public Set<UserGroup> getGroups() {
			return groups;
		}

		public void setGroups(Set<UserGroup> groups) {
			this.groups = groups;
		}
	}

}
