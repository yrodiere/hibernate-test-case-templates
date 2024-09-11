package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Foo {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "textcol")
	private String text;
}
