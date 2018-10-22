/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
@Access(AccessType.PROPERTY)
public class MyIdClass implements Serializable {
	private Integer myField1;
	private Integer myField2;
	private transient Integer myTransientField;

	protected MyIdClass() {
		// For Hibernate ORM
	}

	public MyIdClass(int myField1, int myField2) {
		this.myField1 = myField1;
		this.myField2 = myField2;
		updateTransientField();
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		MyIdClass myIdClass = (MyIdClass) o;
		return Objects.equals( myField1, myIdClass.myField1 ) &&
				Objects.equals( myField2, myIdClass.myField2 );
	}

	@Override
	public int hashCode() {
		return Objects.hash( myField1, myField2 );
	}

	public Integer getMyField1() {
		return myField1;
	}

	protected void setMyField1(Integer value) {
		this.myField1 = value;
		updateTransientField();
	}

	public Integer getMyField2() {
		return myField2;
	}

	protected void setMyField2(Integer value) {
		this.myField2 = value;
		updateTransientField();
	}

	@Transient
	public Integer getMyTransientField() {
		return myTransientField;
	}

	private void updateTransientField() {
		if ( myField1 != null && myField2 != null ) {
			this.myTransientField = myField1 + myField2;
		}
	}
}
