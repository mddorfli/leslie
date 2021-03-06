package org.leslie.server.security;

import java.security.AllPermission;
import java.security.Permission;
import java.security.Permissions;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;
import org.leslie.server.entity.Role;
import org.leslie.server.entity.User;
import org.leslie.server.jpa.JPA;
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

    private static final Logger LOG = LoggerFactory.getLogger(ServerAccessControlService.class);

    @Override
    protected Permissions execLoadPermissions(String userId) {
	final Permissions permissions = new Permissions();

	// calling services is allowed
	permissions.add(new RemoteServiceAccessPermission("*.shared.*", "*"));

	TypedQuery<User> query = JPA.createNamedQuery(User.QUERY_ALL_FETCH_PERMISSIONS, User.class);
	query.setParameter("username", userId);
	User user = query.getResultList().stream()
		.findAny()
		.orElse(null);

	if (user != null && user.getId() == 1L || Platform.get().inDevelopmentMode()) {
	    // admin user always has all permissions
	    permissions.add(new AllPermission());
	    LOG.info("Admin user granted all permissions!");

	} else if (user != null) {
	    final Map<String, Class<? extends Permission>> classesBySimpleName = BEANS.get(IPermissionService.class)
		    .getAllPermissionClasses().stream()
		    .filter(BasicHierarchyPermission.class::isAssignableFrom)
		    .collect(Collectors.toMap(Class::getSimpleName, Function.identity()));

	    user.getRoles().stream()
		    .map(Role::getRolePermissions)
		    .flatMap(Collection::stream)
		    .map(rolePermission -> {
			Class<? extends Permission> clazz = classesBySimpleName.get(rolePermission.getPermissionName());
			Optional<BasicHierarchyPermission> permission = getInstance(clazz);
			if (permission.isPresent()) {
			    permission.get().setLevel(rolePermission.getLevelUid());
			}
			return permission;

		    })
		    .filter(Optional::isPresent)
		    .map(Optional::get)
		    .forEach(permissions::add);
	}

	return permissions;
    }

    private static Optional<BasicHierarchyPermission> getInstance(Class<?> permissionClass) {
	Optional<BasicHierarchyPermission> newInstance;
	try {
	    newInstance = Optional.of((BasicHierarchyPermission) permissionClass.newInstance());
	} catch (InstantiationException | IllegalAccessException e) {
	    newInstance = Optional.empty();
	}
	return newInstance;
    }
}
