package com;

import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Foo {

	@Column(name = "filedata", length = 1024 * 1024)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public Blob blob;

	@Id
	@GeneratedValue
	private int id;

}