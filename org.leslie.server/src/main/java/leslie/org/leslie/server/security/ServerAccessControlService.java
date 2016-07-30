package leslie.org.leslie.server.security;

import java.security.AllPermission;
import java.security.Permissions;
import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.leslie.server.jpa.StoredRole;
import org.leslie.server.jpa.StoredUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.security.AccessControlService;

/**
 * <h3>{@link AccessControlService}</h3>
 *
 * @author kiwi
 */
@Replace
public class ServerAccessControlService extends AccessControlService {

	private static final Logger logger = LoggerFactory.getLogger(ServerAccessControlService.class);

	@Override
	protected Permissions execLoadPermissions(String userId) {
		final Permissions permissions = new Permissions();

		// calling services is allowed
		permissions.add(new RemoteServiceAccessPermission("*.shared.*", "*"));

		TypedQuery<StoredUser> query = JPA.createQuery(""
				+ "SELECT u "
				+ "  FROM " + StoredUser.class.getSimpleName() + " u "
				+ "  LEFT OUTER JOIN FETCH u.roles r "
				+ "  LEFT OUTER JOIN FETCH r.rolePermissions rp "
				+ " WHERE u.username = :username ",
				StoredUser.class);
		query.setParameter("username", userId);
		Optional<StoredUser> user = query.getResultList().stream().findAny();

		if (user.isPresent() && user.get().getId() == 1L) {
			// admin user always has all permissions
			permissions.add(new AllPermission());

		} else if (user.isPresent()) {
			user.get().getRoles().stream()
					.map(StoredRole::getRolePermissions)
					.flatMap(List::stream)
					.forEach(rolePermission -> {
						try {
							BasicHierarchyPermission permission = (BasicHierarchyPermission) Class
									.forName(rolePermission.getPermissionClassName()).newInstance();
							permission.setLevel(rolePermission.getLevelUid());
							permissions.add(permission);

						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
							logger.error("Could not instantate instance of " + rolePermission.getPermissionClassName(),
									e);
						}
					});
		}

		return permissions;
	}
}
