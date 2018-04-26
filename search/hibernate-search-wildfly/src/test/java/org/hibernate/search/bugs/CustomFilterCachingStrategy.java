/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.search.filter.FilterCachingStrategy;
import org.hibernate.search.filter.FilterKey;

import org.apache.lucene.search.Filter;

public class CustomFilterCachingStrategy implements FilterCachingStrategy {
	private static final Statistics STATISTICS = new Statistics();

	public static class Statistics {
		private int hits = 0;
		private int misses = 0;

		public int getHits() {
			return hits;
		}

		public int getMisses() {
			return misses;
		}
	}

	public static Statistics getStatistics() {
		return STATISTICS;
	}

	private Map<FilterKey, Filter> cache = new HashMap<>();

	@Override
	public void initialize(Properties properties) {
	}

	@Override
	public Filter getCachedFilter(FilterKey filterKey) {
		Filter result = cache.get( filterKey );
		if ( result != null ) {
			++STATISTICS.hits;
		}
		else {
			++STATISTICS.misses;
		}
		return result;
	}

	@Override
	public void addCachedFilter(FilterKey filterKey, Filter filter) {
		cache.put( filterKey, filter );
	}
}
