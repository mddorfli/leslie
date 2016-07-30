package leslie.org.leslie.server.admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.StoredRole;
import org.leslie.server.jpa.StoredRolePermission;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.admin.IRoleService;
import leslie.org.leslie.shared.admin.RoleFormData;
import leslie.org.leslie.shared.security.ReadAdministrationPermission;
import leslie.org.leslie.shared.security.UpdateAdministrationPermission;

@Bean
public class RoleService implements IRoleService {

	@Override
	public RoleFormData create(RoleFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredRole role = new StoredRole();
		role.setName(formData.getName().getValue());
		JPA.persist(role);
		return formData;
	}

	@Override
	public RoleFormData store(RoleFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredRole role = JPA.find(StoredRole.class, formData.getRoleNr());
		role.setName(formData.getName().getValue());
		JPA.merge(role);
		return formData;
	}

	@Override
	public void delete(Long selectedValue) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredRole role = JPA.find(StoredRole.class, selectedValue.longValue());
		JPA.remove(role);
	}

	@Override
	public Map<Long, String> getRoleMenuItems() throws ProcessingException {
		if (!ACCESS.check(new ReadAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		return JPA.createQuery(""
				+ "SELECT r FROM " + StoredRole.class.getSimpleName() + " r ",
				StoredRole.class)
				.getResultList()
				.stream()
				.collect(Collectors.toMap(StoredRole::getId, StoredRole::getName));
	}

	@Override
	public void assignPermissions(Long roleId, List<String> permissions, Integer level) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		final StoredRole role = JPA.find(StoredRole.class, roleId);
		for (String permissionClassName : permissions) {
			Optional<StoredRolePermission> existing = JPA.createQuery(""
					+ "SELECT rp "
					+ "  FROM " + StoredRolePermission.class.getSimpleName() + " rp "
					+ " WHERE rp.role = :role "
					+ "   AND rp.permissionClassName = :permissionClassName ",
					StoredRolePermission.class)
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
				StoredRolePermission rp = new StoredRolePermission();
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
		for (String permissionClassName : permissions) {
			JPA.createQuery(""
					+ "SELECT rp "
					+ "  FROM " + StoredRolePermission.class.getSimpleName() + " rp "
					+ " WHERE rp.role.id = :roleId "
					+ "   AND rp.permissionClassName = :permissionClassName ",
					StoredRolePermission.class)
					.setParameter("roleId", roleId)
					.setParameter("permissionClassName", permissionClassName)
					.getResultList()
					.stream().findAny()
					.ifPresent(JPA::remove);;
		}
	}

}
