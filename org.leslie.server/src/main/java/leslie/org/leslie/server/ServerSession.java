package leslie.org.leslie.server;

import java.util.Date;
import java.util.Optional;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.server.AbstractServerSession;
import org.eclipse.scout.rt.server.session.ServerSessionProvider;
import org.leslie.server.jpa.StoredUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link ServerSession}</h3>
 *
 * @author kiwi
 */
public class ServerSession extends AbstractServerSession {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ServerSession.class);

	public ServerSession() {
		super(true);
	}

	/**
	 * @return The {@link ServerSession} which is associated with the current
	 *         thread, or <code>null</code> if not found.
	 */
	public static ServerSession get() {
		return ServerSessionProvider.currentSession(ServerSession.class);
	}

	public StoredUser getUser() {
		TypedQuery<StoredUser> query = JPA.createQuery(""
				+ "SELECT u "
				+ "  FROM " + StoredUser.class.getSimpleName() + " u "
				+ " WHERE u.id = :id ",
				StoredUser.class);
		query.setParameter("id", getUserNr());
		return query.getSingleResult();
	}

	public Long getUserNr() {
		return getSharedContextVariable("userNr", Long.class);
	}

	private void setUserNrInternal(Long userNr) {
		setSharedContextVariable("userNr", Long.class, userNr);
	}

	@Override
	protected void execLoadSession() {
		TypedQuery<StoredUser> query = JPA.createQuery(""
				+ "SELECT u "
				+ "  FROM " + StoredUser.class.getSimpleName() + " u "
				+ " WHERE u.username = :username ",
				StoredUser.class);
		query.setParameter("username", getUserId());
		Optional<StoredUser> user = query.getResultList().stream().findAny();
		if (user.isPresent()) {
			setUserNrInternal(Long.valueOf(user.get().getId()));
			user.get().setLastLogin(new Date(System.currentTimeMillis()));
			user.get().setFailedLoginAttempts(0);
			JPA.merge(user.get());
		}

		LOG.info("created a new session for {}", getUserId());
	}
}
