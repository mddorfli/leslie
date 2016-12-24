package org.leslie.server.security;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AllPermission;
import java.security.Permissions;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.leslie.server.jpa.EntityManagerService;
import org.leslie.server.jpa.entity.Role;
import org.leslie.server.jpa.entity.RolePermission;
import org.leslie.server.jpa.entity.User;
import org.leslie.shared.security.AccessControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link AccessControlService}</h3>
 *
 * @author Marco Dörfliger
 */
@Replace
public class ServerAccessControlService extends AccessControlService {

    private static final Logger logger = LoggerFactory.getLogger(ServerAccessControlService.class);

    @Override
    protected Permissions execLoadPermissions(String userId) {
	final Permissions permissions = new Permissions();

	// calling services is allowed
	permissions.add(new RemoteServiceAccessPermission("*.shared.*", "*"));

	EntityManagerFactory factory = BEANS.get(EntityManagerService.class).getEntityManagerFactory();
	EntityManager em = null;
	Optional<User> user = Optional.empty();
	try {
	    em = factory.createEntityManager();
	    TypedQuery<User> query = em.createQuery(""
		    + "SELECT u "
		    + "  FROM " + User.class.getSimpleName() + " u "
		    + "  LEFT OUTER JOIN FETCH u.roles r "
		    + "  LEFT OUTER JOIN FETCH r.rolePermissions rp "
		    + " WHERE u.username = :username ",
		    User.class);
	    query.setParameter("username", userId);
	    user = query.getResultList().stream().findAny();
	} finally {
	    if (em != null) {
		em.close();
	    }
	}

	if (user.isPresent() && user.get().getId() == 1L || Platform.get().inDevelopmentMode()) {
	    // admin user always has all permissions
	    permissions.add(new AllPermission());

	} else if (user.isPresent()) {
	    for (Role role : user.get().getRoles()) {
		for (RolePermission rolePermission : role.getRolePermissions()) {
		    try {
			@SuppressWarnings("unchecked")
			Class<BasicHierarchyPermission> clazz = (Class<BasicHierarchyPermission>) Class
				.forName(rolePermission.getPermissionClassName());
			Constructor<BasicHierarchyPermission> zeroArgConstructor = clazz.getDeclaredConstructor();
			BasicHierarchyPermission permission = zeroArgConstructor.newInstance();
			permissions.add(permission);

		    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
			// ignore
		    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error("Could not instantate instance of " + rolePermission.getPermissionClassName(), e);
		    }
		}
	    }
	}

	return permissions;
    }
}
