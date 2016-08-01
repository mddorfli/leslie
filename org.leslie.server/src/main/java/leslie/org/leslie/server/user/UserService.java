package leslie.org.leslie.server.user;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.platform.util.Base64Utility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.StoredRole;
import org.leslie.server.jpa.StoredUser;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.admin.UserFormData;
import leslie.org.leslie.shared.admin.UserPageData;
import leslie.org.leslie.shared.admin.UserPageData.UserRowData;
import leslie.org.leslie.shared.security.ReadAdministrationPermission;
import leslie.org.leslie.shared.security.UpdateAdministrationPermission;
import leslie.org.leslie.shared.user.IUserService;

@Bean
public class UserService implements IUserService {

	@Override
	public UserFormData create(UserFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = new StoredUser();
		exportFormData(formData, user);
		applyPassword(user, formData.getPassword().getValue());

		JPA.persist(user);
		return formData;
	}

	@Override
	public UserFormData load(UserFormData formData) throws ProcessingException {
		if (!ACCESS.check(new ReadAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = JPA.find(StoredUser.class, formData.getUserNr());
		importFormData(formData, user);
		return formData;
	}

	@Override
	public UserFormData store(UserFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = JPA.find(StoredUser.class, formData.getUserNr());
		exportFormData(formData, user);
		if (!StringUtility.isNullOrEmpty(formData.getPassword().getValue())) {
			applyPassword(user, formData.getPassword().getValue());
		}
		JPA.merge(user);
		return formData;
	}

	@Override
	public void delete(Long selectedValue) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = JPA.find(StoredUser.class, selectedValue);
		JPA.remove(user);
	}

	private static void exportFormData(UserFormData formData, StoredUser user) {
		user.setUsername(formData.getUsername().getValue());
		user.setFirstName(formData.getFirstName().getValue());
		user.setLastName(formData.getLastName().getValue());
		user.setEmail(formData.getEmail().getValue());
		user.setBlocked(formData.getBlocked().getValue());

		if (formData.getRoles().getValue() != null) {
			user.setRoles(formData.getRoles().getValue().stream()
					.map(id -> JPA.find(StoredRole.class, id))
					.collect(Collectors.toList()));
		}

	}

	private static void importFormData(UserFormData formData, StoredUser user) {
		formData.getUsername().setValue(user.getUsername());
		formData.getFirstName().setValue(user.getFirstName());
		formData.getLastName().setValue(user.getLastName());
		formData.getEmail().setValue(user.getEmail());
		formData.getBlocked().setValue(user.isBlocked());

		if (formData.getRoles().getValue() != null) {
			formData.getRoles().setValue(user.getRoles().stream()
					.map(StoredRole::getId)
					.collect(Collectors.toSet()));
		}
	}

	private static void applyPassword(StoredUser user, String password) {
		byte[] salt = SecurityUtility.createRandomBytes();
		byte[] hash = SecurityUtility.hash(Base64Utility.decode(password), salt);
		user.setPasswordSalt(Base64Utility.encode(salt));
		user.setPasswordHash(Base64Utility.encode(hash));
	}

	@Override
	public UserPageData getUserTableData(UserPresentationType presentationType, Long projectId) {
		final UserPageData pageData = new UserPageData();
		StringBuilder fromSql = new StringBuilder();
		StringBuilder whereSql = new StringBuilder();
		Map<String, Long> parameters = new HashMap<>();
		switch (presentationType) {
			case PROJECT :
				fromSql.append(" JOIN FETCH u.projects p ");
				whereSql.append(" AND p.id = :projectId ");
				parameters.put("projectId", projectId);
				break;
			case ADMINISTRATION :
			default :
				break;
		}

		TypedQuery<StoredUser> query = JPA.createQuery(""
				+ "SELECT u "
				+ "  FROM " + StoredUser.class.getSimpleName() + " u "
				+ fromSql.toString()
				+ " WHERE 1=1 "
				+ whereSql.toString(),
				StoredUser.class);
		parameters.forEach(query::setParameter);
		query.getResultList().forEach((user) -> exportPageData(pageData.addRow(), user));

		return pageData;
	}

	private static void exportPageData(UserRowData row, StoredUser user) {
		row.setId(user.getId());
		row.setUsername(user.getUsername());
		row.setFirstName(user.getFirstName());
		row.setDisplayName(user.getDisplayName());
		row.setLastName(user.getLastName());
		row.setEmail(user.getEmail());
		row.setLoginAttempts(user.getFailedLoginAttempts());
		row.setBlocked(user.isBlocked());
	}
}
