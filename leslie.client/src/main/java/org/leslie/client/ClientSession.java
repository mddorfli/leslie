package org.leslie.client;

import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.leslie.shared.user.IUserService;

/**
 * <h3>{@link ClientSession}</h3>
 *
 * @author Marco Dörfliger
 */
public class ClientSession extends AbstractClientSession {

	public ClientSession() {
		super(true);
	}

	/**
	 * @return The {@link IClientSession} which is associated with the current
	 *         thread, or <code>null</code> if not found.
	 */
	public static ClientSession get() {
		return ClientSessionProvider.currentSession(ClientSession.class);
	}

	@Override
	protected void execLoadSession() {
		// pre-load all known code types
		CODES.getAllCodeTypes("org.leslie.shared.code");

		setDesktop(new Desktop());
	}

	public Long getUserNr() {
		return BEANS.get(IUserService.class).getCurrentUserNr();
	}

}
