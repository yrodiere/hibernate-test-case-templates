package org.hibernate.bugs;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

public abstract class AbstractTestCase extends BaseCoreFunctionalTestCase {

	public static final String TRUE = Boolean.TRUE.toString();
	public static final String FALSE = Boolean.FALSE.toString();
	private Class[] annotatedClasses;
	private Consumer<Configuration> config;

	@Override
	protected void configure(Configuration configuration) {
		super.configure(configuration);
		if (config != null) {
			config.accept(configuration);
		}

	}

	protected AbstractTestCase configure(Consumer<Configuration> config) {
		this.config = config;
		return this;
	}

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
