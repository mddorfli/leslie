package org.leslie.server.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.transaction.AbstractTransactionMember;
import org.eclipse.scout.rt.server.transaction.ITransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityManagerService implements IService {

	private static final String PERSISTENCE_UNIT_NAME = "leslie";

	private static final String TRANSACTION_ID = EntityManagerService.class.getSimpleName() + ".transaction";

	private static final Logger logger = LoggerFactory.getLogger(EntityManagerService.class);

	private EntityManagerFactory m_entityManagerFactory;
	private EntityManager m_entityManager;

	@PostConstruct
	private void init() {
		Map<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.jdbc.driver", BEANS.get(JdbcDriverClassProperty.class).getValue());
		properties.put("javax.persistence.jdbc.url", BEANS.get(JdbcUrlProperty.class).getValue());
		properties.put("javax.persistence.jdbc.user", BEANS.get(JdbcUsernameProperty.class).getValue());
		properties.put("javax.persistence.jdbc.password", BEANS.get(JdbcPasswordProperty.class).getValue());

		m_entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
		m_entityManager = m_entityManagerFactory.createEntityManager();
	}

	@PreDestroy
	private void destroy() {
		m_entityManager.close();
		m_entityManagerFactory.close();
	}

	public EntityManager getEntityManager() {
		getOrStartTransaction();
		return m_entityManager;
	}

	/**
	 * Starts a jpa transaction (if one has not alredy been started). Will
	 * trigger a jpa flush / commit once the scout transaction is closed.
	 *
	 * @throws ProcessingException
	 */
	private JpaTransactionMember getOrStartTransaction() throws ProcessingException {
		ITransaction reg = Assertions.assertNotNull(ITransaction.CURRENT.get(), "Transaction required");
		if (reg == null) {
			throw new ProcessingException("no ITransaction available, use ServerJob to run truncactions");
		}
		JpaTransactionMember member = (JpaTransactionMember) reg.getMember(TRANSACTION_ID);
		if (member == null) {
			member = new JpaTransactionMember(TRANSACTION_ID);
			reg.registerMember(member);
		}

		return member;
	}

	private class JpaTransactionMember extends AbstractTransactionMember {

		private EntityTransaction tx;

		public JpaTransactionMember(String transactionId) {
			super(transactionId);
			tx = m_entityManager.getTransaction();
			if (tx.isActive()) {
				logger.warn("Transaction should not be active!");
			} else {
				logger.trace("JPA transaction begun.");
				tx.begin();
			}
		}

		@Override
		public boolean needsCommit() {
			return tx.isActive();
		}

		@Override
		public boolean commitPhase1() {
			return true;
		}

		@Override
		public void commitPhase2() {
			logger.trace("JPA transaction committed.");
			tx.commit();
		}

		@Override
		public void rollback() {
			logger.trace("JPA transaction rolled back.");
			tx.rollback();
		}

		@Override
		public void release() {
			// n/a
		}
	}

	public static class JdbcUrlProperty extends AbstractStringConfigProperty {

		@Override
		public String getKey() {
			return "server.jdbc.url";
		}

		@Override
		protected String getDefaultValue() {
			return "jdbc:postgresql://localhost:5432/leslie";
		}
	}

	public static class JdbcDriverClassProperty extends AbstractStringConfigProperty {

		@Override
		public String getKey() {
			return "server.jdbc.driverClass";
		}

		@Override
		protected String getDefaultValue() {
			return "org.postgresql.Driver";
		}
	}

	public static class JdbcUsernameProperty extends AbstractStringConfigProperty {

		@Override
		public String getKey() {
			return "server.jdbc.user";
		}

		@Override
		protected String getDefaultValue() {
			return "leslie";
		}
	}

	public static class JdbcPasswordProperty extends AbstractStringConfigProperty {

		@Override
		public String getKey() {
			return "server.jdbc.password";
		}

		@Override
		protected String getDefaultValue() {
			return "leslie";
		}
	}

}
