package org.hibernate.bugs;

import java.util.List;

import org.hibernate.Session;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Score {
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private int points;

	public Score() {
	}

	public Score(String name, int points) {
		this.name = name;
		this.points = points;
	}

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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
