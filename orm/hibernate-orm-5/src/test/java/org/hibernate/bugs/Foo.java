package org.hibernate.bugs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Foo {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "textcol")
	private String text;
}
