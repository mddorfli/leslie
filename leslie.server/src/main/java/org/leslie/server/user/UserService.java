package org.leslie.server.user;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.platform.util.Base64Utility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.user.UserFormData;
import org.leslie.client.user.UserPageData;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.mapping.FieldMapper;
import org.leslie.shared.security.permission.ReadAdministrationPermission;
import org.leslie.shared.security.permission.UpdateAdministrationPermission;
import org.leslie.shared.user.IUserService;

@Bean
public class UserService implements IUserService {

    @Override
    public UserFormData create(UserFormData formData) throws ProcessingException {
	if (!ACCESS.check(new UpdateAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	User user = new User();
	FieldMapper.exportFormData(formData, user);
	// exportFormData(formData, user);
	applyPassword(user, formData.getPassword().getValue());

	JPA.persist(user);
	return formData;
    }

    @Override
    public UserFormData load(UserFormData formData) throws ProcessingException {
	if (!ACCESS.check(new ReadAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	User user = JPA.find(User.class, formData.getUserNr());
	FieldMapper.importFormData(user, formData);
	return formData;
    }

    @Override
    public UserFormData store(UserFormData formData) throws ProcessingException {
	if (!ACCESS.check(new UpdateAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	User user = JPA.find(User.class, formData.getUserNr());
	FieldMapper.importFormData(user, formData);
	// exportFormData(formData, user);
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
	User user = JPA.find(User.class, selectedValue);
	JPA.remove(user);
    }

    @Override
    public UserPageData getUserTableData(UserPresentationType presentationType, Long projectId) {
	final UserPageData pageData = new UserPageData();
	StringBuilder fromSql = new StringBuilder();
	StringBuilder whereSql = new StringBuilder();
	Map<String, Long> parameters = new HashMap<>();
	switch (presentationType) {
	case PROJECT:
	    // fromSql.append(" JOIN FETCH u.projectAssignments p ");
	    // whereSql.append(" AND p.key.id = :projectId ");
	    // parameters.put("projectId", projectId);
	    break;
	case ADMINISTRATION:
	default:
	    break;
	}

	TypedQuery<User> query = JPA.createQuery(""
		+ "SELECT u "
		+ "  FROM " + User.class.getSimpleName() + " u "
		+ fromSql.toString()
		+ " WHERE 1=1 "
		+ whereSql.toString(),
		User.class);
	parameters.forEach(query::setParameter);

	// Project p = JPA.find(Project.class, projectId);
	// query.getResultList().stream()
	// .filter(user -> user.getProjectAssignments().containsKey(p))
	// .forEach((user) -> exportPageData(pageData.addRow(), user));
	FieldMapper.importTablePageData(query.getResultList(), pageData);

	return pageData;
    }

    private static void applyPassword(User user, String password) {
	byte[] salt = SecurityUtility.createRandomBytes();
	byte[] hash = SecurityUtility.hash(Base64Utility.decode(password), salt);
	user.setPasswordSalt(Base64Utility.encode(salt));
	user.setPasswordHash(Base64Utility.encode(hash));
    }

    // private static void exportFormData(UserFormData formData, User user) {
    // user.setUsername(formData.getUsername().getValue());
    // user.setFirstName(formData.getFirstName().getValue());
    // user.setLastName(formData.getLastName().getValue());
    // user.setEmail(formData.getEmail().getValue());
    // user.setBlocked(formData.getBlocked().getValue());
    //
    // if (formData.getRoles().getValue() != null) {
    // user.setRoles(formData.getRoles().getValue().stream()
    // .map(id -> JPA.find(Role.class, id))
    // .collect(Collectors.toList()));
    // }
    // }

    // private static void importFormData(UserFormData formData, User user) {
    // formData.getUsername().setValue(user.getUsername());
    // formData.getFirstName().setValue(user.getFirstName());
    // formData.getLastName().setValue(user.getLastName());
    // formData.getEmail().setValue(user.getEmail());
    // formData.getBlocked().setValue(user.isBlocked());
    //
    // formData.getRoles().setValue(user.getRoles().stream()
    // .map(Role::getId)
    // .collect(Collectors.toSet()));
    // }

    // private static void exportPageData(UserRowData row, User user) {
    // row.setId(user.getId());
    // row.setUsername(user.getUsername());
    // row.setFirstName(user.getFirstName());
    // row.setDisplayName(user.getDisplayName());
    // row.setLastName(user.getLastName());
    // row.setEmail(user.getEmail());
    // row.setLoginAttempts(user.getFailedLoginAttempts());
    // row.setBlocked(user.isBlocked());
    // }
}
