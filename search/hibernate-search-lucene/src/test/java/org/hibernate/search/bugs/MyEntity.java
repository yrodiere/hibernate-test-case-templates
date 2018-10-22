/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;

@Entity
@Indexed
public class MyEntity {

	@Id
	@EmbeddedId
	@FieldBridge(impl = MyIdClassBridge.class)
	private MyIdClass id;

	@Basic
	@Field
	@Field(name = "sortField", analyze = Analyze.NO)
	@SortableField(forField = "sortField")
	private String textField;

	protected MyEntity() {
		// For Hibernate ORM
	}

	public MyEntity(MyIdClass id, String textField) {
		this.id = id;
		this.textField = textField;
	}

	public MyIdClass getId() {
		return id;
	}

	public void setId(MyIdClass id) {
		this.id = id;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}
}
