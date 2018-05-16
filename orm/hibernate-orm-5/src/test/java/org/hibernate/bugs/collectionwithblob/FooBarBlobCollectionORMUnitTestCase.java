/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.bugs.collectionwithblob;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Assert;
import org.junit.Test;

import com.collectionwithblob.Bar;
import com.collectionwithblob.Foo;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 *
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
public class FooBarBlobCollectionORMUnitTestCase extends BaseCoreFunctionalTestCase {

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Foo.class,
				Bar.class
		};
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh12555() throws Exception {
		// remove Generic from annotated classes as otherweise, hhh12579 will crash bootstrapping!
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		// Do stuff...
		Foo foo = new Foo();
		foo.blob = s.getLobHelper().createBlob("TEST CASE".getBytes());
		s.save(foo);
		s.flush();
		s.clear();
		Foo foo2 = (Foo) s.merge(foo);
		System.err.println("Blob: " + foo.blob);
		tx.commit();
		s.close();
	}

	@Test
	public void hhh12425() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		// Do stuff...
		Bar bar = new Bar();
		bar.foos.add(createFoo(s, bar));
		bar.foos.add(createFoo(s, bar));

		s.save(bar);
		s.flush();
		s.clear();
		Bar newBar = s.get(Bar.class, bar.id);
		Assert.assertEquals(2, newBar.foos.size());
		tx.commit();
		s.close();
	}

	private Foo createFoo(Session s, Bar bar) {
		Foo foo = new Foo();
		foo.blob = s.getLobHelper().createBlob("TEST CASE".getBytes());
		foo.bar = bar;
		s.save(foo);
		return foo;
	}
}
