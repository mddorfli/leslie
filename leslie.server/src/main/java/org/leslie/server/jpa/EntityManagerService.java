package org.leslie.server.jpa;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
		m_entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
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
			logger.trace("JPA transaction begun.");
			tx = m_entityManager.getTransaction();
			if (tx.isActive()) {
				logger.warn("Transaction should not be active!");
			} else {
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
}
