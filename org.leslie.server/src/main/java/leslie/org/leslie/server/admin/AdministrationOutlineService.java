package leslie.org.leslie.server.admin;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.leslie.server.jpa.StoredRole;
import org.leslie.server.jpa.StoredRolePermission;
import org.leslie.server.jpa.StoredUser;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.admin.IAdministrationOutlineService;
import leslie.org.leslie.shared.admin.PermissionTablePageData;
import leslie.org.leslie.shared.admin.PermissionTablePageData.PermissionTableRowData;
import leslie.org.leslie.shared.admin.RolePageData;
import leslie.org.leslie.shared.admin.RolePageData.RoleRowData;
import leslie.org.leslie.shared.admin.UserAdministrationPageData;
import leslie.org.leslie.shared.admin.UserAdministrationPageData.UserAdministrationRowData;

@Bean
public class AdministrationOutlineService implements IAdministrationOutlineService {

	@Override
	public PermissionTablePageData getPermissionTableData(Long roleId) throws ProcessingException {
		final PermissionTablePageData pageData = new PermissionTablePageData();
		JPA.createQuery(""
				+ "SELECT rp "
				+ "  FROM " + StoredRolePermission.class.getSimpleName() + " rp "
				+ " WHERE rp.role.id = :roleId ",
				StoredRolePermission.class)
				.setParameter("roleId", roleId)
				.getResultList()
				.forEach((role) -> copyRowData(role, pageData));

		return pageData;
	}

	private static void copyRowData(StoredRolePermission rolePermission, PermissionTablePageData pageData) {
		PermissionTableRowData row = pageData.addRow();
		row.setLevel(rolePermission.getLevelUid());
		row.setName(rolePermission.getPermissionClassName());
	}

	@Override
	public RolePageData getRoleTableData() throws ProcessingException {
		final RolePageData pageData = new RolePageData();
		JPA.createQuery("SELECT r FROM " + StoredRole.class.getSimpleName() + " r ",
				StoredRole.class)
				.getResultList()
				.forEach((src) -> copyRowData(src, pageData));

		return pageData;
	}

	private static void copyRowData(StoredRole role, RolePageData pageData) {
		RoleRowData row = pageData.addRow();
		row.setRoleNr(role.getId());
		row.setRoleName(role.getName());
	}

	@Override
	public UserAdministrationPageData getUserTableData() throws ProcessingException {
		final UserAdministrationPageData pageData = new UserAdministrationPageData();
		JPA.createQuery(""
				+ "SELECT u "
				+ "  FROM " + StoredUser.class.getSimpleName() + " u ",
				StoredUser.class)
				.getResultList()
				.forEach((user) -> copyPageData(user, pageData));

		return pageData;
	}

	private static void copyPageData(StoredUser user, UserAdministrationPageData pageData) {
		UserAdministrationRowData row = pageData.addRow();
		row.setId(user.getId());
		row.setUsername(user.getUsername());
		row.setFirstName(user.getFirstName());
		row.setLastName(user.getLastName());
		row.setEmail(user.getEmail());
		row.setLoginAttempts(user.getFailedLoginAttempts());
		row.setBlocked(user.isBlocked());
	}

}
