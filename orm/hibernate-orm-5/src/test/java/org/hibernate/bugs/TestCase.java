package org.hibernate.bugs;

import org.junit.Test;

import models.specific.Generic;

public class TestCase extends AbstractTestCase {
	public TestCase() {
		super(Generic.class);
	}

	@Test
	public void hhh12579() {
		System.out.println("Just execute, it will fail during bootstrapping");
	}
}
