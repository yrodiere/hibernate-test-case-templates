/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.FullTextFilter;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.search.Filter;

public class NameFilterFactory {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@Factory
	public Filter create() {
		return new TermFilter( new Term( "name", name ) );
	}
}
