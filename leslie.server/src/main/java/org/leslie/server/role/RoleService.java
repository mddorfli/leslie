package org.leslie.server.role;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.permission.PermissionTablePageData;
import org.leslie.client.role.RoleFormData;
import org.leslie.client.role.RolePageData;
import org.leslie.server.entity.Role;
import org.leslie.server.entity.RolePermission;
import org.leslie.server.entity.User;
import org.leslie.server.jpa.JPA;
import org.leslie.server.mapping.MappingUtility;
import org.leslie.server.security.ServerAccessControlService;
import org.leslie.shared.role.IRoleService;
import org.leslie.shared.security.permission.ReadAdministrationPermission;
import org.leslie.shared.security.permission.UpdateAdministrationPermission;

@Bean
public class RoleService implements IRoleService {

	@Override
	public RolePageData getRoleTableData() throws ProcessingException {
		RolePageData pageData = new RolePageData();
		List<Role> roles = JPA.createNamedQuery(Role.QUERY_ALL, Role.class).getResultList();
		MappingUtility.importTablePageData(roles, pageData);
		return pageData;
	}

	@Override
	public PermissionTablePageData getPermissionTableData(Long roleId) {
		final PermissionTablePageData pageData = new PermissionTablePageData();
		List<RolePermission> rolePermissions = JPA
				.createNamedQuery(RolePermission.QUERY_BY_ROLE_ID, RolePermission.class)
				.setParameter("roleId", roleId)
				.getResultList();
		MappingUtility.importTablePageData(rolePermissions, pageData);
		return pageData;
	}

	@Override
	public RoleFormData create(RoleFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = new Role();
		MappingUtility.exportFormData(formData, role);
		JPA.persist(role);
		return formData;
	}

	@Override
	public RoleFormData store(RoleFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = JPA.find(Role.class, formData.getRoleId());
		MappingUtility.exportFormData(formData, role);
		return formData;
	}

	@Override
	public void delete(Long roleId) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = JPA.find(Role.class, roleId.longValue());
		// remove all permissions from the role
		JPA.createNamedQuery(RolePermission.QUERY_BY_ROLE_ID, RolePermission.class)
				.setParameter("roleId", role.getId())
				.getResultList()
				.forEach(JPA::remove);

		// remove this role from all users
		JPA.createNamedQuery(User.QUERY_BY_ROLE_ID, User.class)
				.setParameter("roleId", role.getId())
				.getResultList()
				.forEach(user -> {
					user.getRoles().removeIf(userRole -> userRole.getId() == roleId);
				});

		JPA.remove(role);

		BEANS.get(ServerAccessControlService.class).clearCache();
	}

	@Override
	public Map<Long, String> getRoleMenuItems() throws ProcessingException {
		if (!ACCESS.check(new ReadAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		return JPA.createNamedQuery(Role.QUERY_ALL, Role.class)
				.getResultList().stream()
				.collect(Collectors.toMap(Role::getId, Role::getName));
	}

	@Override
	public void assignPermissions(Long roleId, List<String> permissions, Integer level) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		final Role role = JPA.find(Role.class, roleId);
		for (String permissionName : permissions) {
			Optional<RolePermission> existing = JPA.createNamedQuery(
					RolePermission.QUERY_BY_ROLE_ID_AND_PERMISSION_NAME, RolePermission.class)
					.setParameter("roleId", role.getId())
					.setParameter("permissionName", permissionName)
					.getResultList()
					.stream().findAny();
			if (existing.isPresent()) {
				// update
				existing.get().setLevelUid(level);
			} else {
				// insert
				RolePermission rp = new RolePermission();
				rp.setRole(role);
				rp.setPermissionName(permissionName);
				rp.setLevelUid(level);
				JPA.persist(rp);
			}
		}
		BEANS.get(ServerAccessControlService.class).clearCache();
	}

	@Override
	public void revokePermissions(Long roleId, List<String> permissionNames) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = JPA.find(Role.class, roleId);
		role.getRolePermissions().removeIf(permission -> permissionNames.contains(permission.getPermissionName()));

		BEANS.get(ServerAccessControlService.class).clearCache();
	}
}
