/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.bugs.util;

import java.util.function.Consumer;

import org.hibernate.search.backend.spi.BackendQueueProcessor;

/**
 * An unused class that uses classes specific to Hibernate Search 5.5.
 * If we are not using Hibernate Search 5.5 at compile time,
 * compilation will fail thanks to this class.
 */
public class EnsureCompilingForSearch55 {

	public static final Consumer<BackendQueueProcessor> methodThatGotRemovedInSearch56 =
			BackendQueueProcessor::indexMappingChanged;

}
