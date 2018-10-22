/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import org.apache.lucene.document.Document;

public class MyIdClassBridge implements TwoWayFieldBridge {

	private static final String STRING_THAT_NEVER_APPEARS_IN_FIELD_1 = "/";
	private static final String STRING_THAT_NEVER_APPEARS_IN_FIELD_1_REGEX = "\\/";

	@Override
	public Object get(String name, Document document) {
		String idAsString = document.get( name );
		if ( idAsString == null ) {
			return null;
		}
		String[] split = idAsString.split( STRING_THAT_NEVER_APPEARS_IN_FIELD_1_REGEX, 2 );
		return new MyIdClass( Integer.parseInt( split[0] ), Integer.parseInt( split[1] ) );
	}

	@Override
	public String objectToString(Object value) {
		if ( value == null ) {
			return null;
		}
		MyIdClass id = (MyIdClass) value;
		return id.getMyField1() + STRING_THAT_NEVER_APPEARS_IN_FIELD_1 + id.getMyField2();
	}

	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		luceneOptions.addFieldToDocument( name, objectToString( value ), document );
	}
}
