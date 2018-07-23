package org.hibernate.bugs;

import java.util.Locale;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.internal.util.StringHelper;

/**
 * @see <A HREF="https://stackoverflow.com/questions/32437202/improvednamingstrategy-no-longer-working-in-hibernate-5">
 * ImprovedNamingStrategy no longer working in Hibernate 5</A>
 */
public class ImprovedPhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl {
	private static final long serialVersionUID = -5520697659246884475L;

	public ImprovedPhysicalNamingStrategyImpl() {
		super();
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		return addUnderscores(name);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		return addUnderscores(name);
	}

	public static Identifier addUnderscores(Identifier name) {
		String normal = normalize(name.getText());
		return new Identifier(normal, name.isQuoted());
	}

	public static String normalize(String name) {
		String pureName = StringHelper.unqualify(name);
		return addUnderscores(pureName);
	}

	public static String addUnderscores(String name) {
		StringBuilder buf = new StringBuilder(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++) {
			if (Character.isLowerCase(buf.charAt(i - 1))
					&& Character.isUpperCase(buf.charAt(i))
					&& Character.isLowerCase(buf.charAt(i + 1))) {
				buf.insert(i++, '_');
			}
		}

		return buf.toString().toLowerCase(Locale.ROOT);
	}
}
