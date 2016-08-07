package org.leslie.server.jpa;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

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

	@PostConstruct
	private void init() {
		m_entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return m_entityManagerFactory;
	}

	@PrePersist
	void onPrePersist(Object o) throws ProcessingException {
		getOrStartTransaction();
		logger.debug("onPrePersist - starting transaction: {}", o);
	}

	@PostPersist
	void onPostPersist(Object o) {
		logger.debug("onPostPersist");
	}

	@PostLoad
	void onPostLoad(Object o) {
		logger.debug("onPostLoad");
	}

	@PreUpdate
	void onPreUpdate(Object o) throws ProcessingException {
		getOrStartTransaction();
		logger.debug("onPreUpdate - starting transaction: {}", o);
	}

	@PostUpdate
	void onPostUpdate(Object o) {
		logger.debug("onPostUpdate");
	}

	@PreRemove
	void onPreRemove(Object o) throws ProcessingException {
		getOrStartTransaction();
		logger.debug("onPreRemove - starting transaction: {}", o);
	}

	@PostRemove
	void onPostRemove(Object o) {
		logger.debug("onPostRemove");
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

	public EntityManager getEntityManager() {
		JpaTransactionMember member = getOrStartTransaction();
		return member.entityManager;
	}

	private class JpaTransactionMember extends AbstractTransactionMember {

		private EntityManager entityManager;

		public JpaTransactionMember(String transactionId) {
			super(transactionId);
			entityManager = m_entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
		}

		@Override
		public boolean needsCommit() {
			return true;
		}

		@Override
		public boolean commitPhase1() {
			return true;
		}

		@Override
		public void commitPhase2() {
			entityManager.flush();
			entityManager.getTransaction().commit();
		}

		@Override
		public void rollback() {
			entityManager.getTransaction().rollback();
		}

		@Override
		public void release() {
			entityManager.close();
		}
	}
}
