package org.leslie.server;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.server.AbstractServerSession;
import org.eclipse.scout.rt.server.session.ServerSessionProvider;
import org.leslie.server.entity.User;
import org.leslie.server.jpa.JPA;
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
	return JPA.find(User.class, getUserNr());
    }

    public Long getUserNr() {
	return getSharedContextVariable("userNr", Long.class);
    }

    private void setUserNrInternal(Long userNr) {
	setSharedContextVariable("userNr", Long.class, userNr);
    }

    @Override
    protected void execLoadSession() {
	User user = JPA.createNamedQuery(User.QUERY_BY_USERNAME, User.class)
		.setParameter("username", getUserId())
		.getResultList().stream()
		.findAny()
		.orElseThrow(() -> new SecurityException(
			new ProcessingException("Could not find user with username {}", (Object) getUserId())));

	setUserNrInternal(Long.valueOf(user.getId()));
	user.setLastLogin(new Date(System.currentTimeMillis()));
	user.setFailedLoginAttempts(0);

	LOG.info("created a new session for {}", getUserId());
    }
}
