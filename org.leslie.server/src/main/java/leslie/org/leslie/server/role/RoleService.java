package leslie.org.leslie.server.role;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.Role;
import org.leslie.server.jpa.RolePermission;
import org.leslie.server.jpa.User;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.admin.PermissionTablePageData;
import leslie.org.leslie.shared.admin.PermissionTablePageData.PermissionTableRowData;
import leslie.org.leslie.shared.admin.RoleFormData;
import leslie.org.leslie.shared.admin.RolePageData;
import leslie.org.leslie.shared.admin.RolePageData.RoleRowData;
import leslie.org.leslie.shared.role.IRoleService;
import leslie.org.leslie.shared.security.permission.ReadAdministrationPermission;
import leslie.org.leslie.shared.security.permission.UpdateAdministrationPermission;

@Bean
public class RoleService implements IRoleService {

	@Override
	public RolePageData getRoleTableData() throws ProcessingException {
		final RolePageData pageData = new RolePageData();
		JPA.createQuery("SELECT r FROM " + Role.class.getSimpleName() + " r ",
				Role.class)
				.getResultList()
				.forEach((src) -> exportRowData(pageData.addRow(), src));

		return pageData;
	}

	private static void exportRowData(RoleRowData row, Role role) {
		row.setRoleNr(role.getId());
		row.setRoleName(role.getName());
	}

	@Override
	public PermissionTablePageData getPermissionTableData(Long roleId) {
		final PermissionTablePageData pageData = new PermissionTablePageData();
		JPA.createQuery(""
				+ "SELECT rp "
				+ "  FROM " + RolePermission.class.getSimpleName() + " rp "
				+ " WHERE rp.role.id = :roleId ",
				RolePermission.class)
				.setParameter("roleId", roleId)
				.getResultList()
				.forEach((role) -> importRowData(role, pageData.addRow()));

		return pageData;
	}

	private static void importRowData(RolePermission rolePermission, PermissionTableRowData row) {
		row.setLevel(rolePermission.getLevelUid());
		row.setName(rolePermission.getPermissionClassName());
	}

	@Override
	public RoleFormData create(RoleFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = new Role();
		mapFields(formData, role);
		JPA.persist(role);
		return formData;
	}

	private static void mapFields(RoleFormData formData, Role role) {
		role.setName(formData.getName().getValue());
	}

	@Override
	public RoleFormData store(RoleFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = JPA.find(Role.class, formData.getRoleNr());
		mapFields(formData, role);
		JPA.merge(role);
		return formData;
	}

	@Override
	public void delete(Long roleId) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = JPA.find(Role.class, roleId.longValue());
		// remove all permissions from the role
		JPA.createQuery(""
				+ "DELETE FROM " + RolePermission.class.getSimpleName() + " c "
				+ " WHERE c.role = :role ",
				RolePermission.class)
				.setParameter("role", role);

		// remove this role from all users
		JPA.createQuery(""
				+ "SELECT u "
				+ " FROM " + User.class.getSimpleName() + " u "
				+ " JOIN u.roles r "
				+ " WHERE r = :role ",
				User.class)
				.setParameter("role", role)
				.getResultList()
				.forEach(user -> {
					user.getRoles().removeIf(userRole -> userRole.getId() == roleId);
					JPA.merge(user);
				});

		JPA.remove(role);
	}

	@Override
	public Map<Long, String> getRoleMenuItems() throws ProcessingException {
		if (!ACCESS.check(new ReadAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		return JPA.createQuery(""
				+ "SELECT r FROM " + Role.class.getSimpleName() + " r ",
				Role.class)
				.getResultList()
				.stream()
				.collect(Collectors.toMap(Role::getId, Role::getName));
	}

	@Override
	public void assignPermissions(Long roleId, List<String> permissions, Integer level) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		final Role role = JPA.find(Role.class, roleId);
		for (String permissionClassName : permissions) {
			Optional<RolePermission> existing = JPA.createQuery(""
					+ "SELECT rp "
					+ "  FROM " + RolePermission.class.getSimpleName() + " rp "
					+ " WHERE rp.role = :role "
					+ "   AND rp.permissionClassName = :permissionClassName ",
					RolePermission.class)
					.setParameter("role", role)
					.setParameter("permissionClassName", permissionClassName)
					.getResultList()
					.stream().findAny();
			if (existing.isPresent()) {
				// update
				existing.get().setLevelUid(level);
				JPA.merge(existing.get());
			} else {
				// insert
				RolePermission rp = new RolePermission();
				rp.setRole(role);
				rp.setPermissionClassName(permissionClassName);
				rp.setLevelUid(level);
				JPA.persist(rp);
			}
		}
	}

	@Override
	public void revokePermissions(Long roleId, List<String> permissions) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Role role = JPA.find(Role.class, roleId);
		role.getRolePermissions().removeIf(
				rolePermission -> permissions.contains(rolePermission.getPermissionClassName()));
		JPA.merge(role);
	}
}
