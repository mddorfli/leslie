package leslie.org.leslie.server;

import java.util.Date;
import java.util.Optional;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.server.AbstractServerSession;
import org.eclipse.scout.rt.server.session.ServerSessionProvider;
import org.leslie.server.jpa.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link ServerSession}</h3>
 *
 * @author Marco Dörfliger
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

	public User getUser() {
		TypedQuery<User> query = JPA.createQuery(""
				+ "SELECT u "
				+ "  FROM " + User.class.getSimpleName() + " u "
				+ " WHERE u.id = :id ",
				User.class);
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
		TypedQuery<User> query = JPA.createQuery(""
				+ "SELECT u "
				+ "  FROM " + User.class.getSimpleName() + " u "
				+ " WHERE u.username = :username ",
				User.class);
		query.setParameter("username", getUserId());
		Optional<User> user = query.getResultList().stream().findAny();
		if (user.isPresent()) {
			setUserNrInternal(Long.valueOf(user.get().getId()));
			user.get().setLastLogin(new Date(System.currentTimeMillis()));
			user.get().setFailedLoginAttempts(0);
			JPA.merge(user.get());
		}

		LOG.info("created a new session for {}", getUserId());
	}
}
