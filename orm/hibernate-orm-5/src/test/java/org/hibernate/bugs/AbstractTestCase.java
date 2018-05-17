package org.hibernate.bugs;

import java.util.function.BiConsumer;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

public abstract class AbstractTestCase extends BaseCoreFunctionalTestCase {

	private Class[] annotatedClasses;

	protected AbstractTestCase(Class<?> mandatoryClass, Class... additionalClasses) {
		Class[] annotatedClasses = new Class[additionalClasses.length + 1];
		annotatedClasses[0] = mandatoryClass;
		for (int i = 1; i < annotatedClasses.length; i++) {
			annotatedClasses[i] = additionalClasses[i - 1];
		}
		this.annotatedClasses = annotatedClasses;
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return annotatedClasses;
	}

	protected void doInOpenTransaction(BiConsumer<Session, Transaction> code) {

		try (Session s = openSession()) {
			Transaction tx = s.beginTransaction();
			code.accept(s, tx);
			if (tx.getStatus().isOneOf(TransactionStatus.ACTIVE)) {
				tx.commit();
			}
		}
	}
}
