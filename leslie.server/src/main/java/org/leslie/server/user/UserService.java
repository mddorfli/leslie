package org.leslie.server.user;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.leslie.client.user.UserPageData.UserRowData;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Project;
import org.leslie.server.jpa.entity.Role;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.mapping.FieldMapper;
import org.leslie.shared.security.permission.ReadAdministrationPermission;
import org.leslie.shared.security.permission.ReadProjectPermission;
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
	if (user.getRoles() != null) {
	    Set<Long> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
	    formData.getRoles().setValue(roleIds);
	}
	return formData;
    }

    @Override
    public UserFormData store(UserFormData formData) throws ProcessingException {
	if (!ACCESS.check(new UpdateAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	User user = JPA.find(User.class, formData.getUserNr());
	FieldMapper.exportFormData(formData, user);

	final Set<Long> selectedRoleIds = formData.getRoles().getValue();
	if (selectedRoleIds == null || selectedRoleIds.isEmpty()) {
	    user.getRoles().clear();
	} else {
	    user.getRoles().removeIf(role -> {
		return !selectedRoleIds.contains(Long.valueOf(role.getId()));
	    });
	    List<Role> newRoles = JPA.createQuery(""
		    + "SELECT r "
		    + " FROM " + Role.class.getSimpleName() + " r "
		    + " WHERE r.id IN :roleIds ",
		    Role.class)
		    .setParameter("roleIds", selectedRoleIds)
		    .getResultList();
	    user.getRoles().addAll(newRoles);
	}

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
	// TODO remove links
	JPA.remove(user);
    }

    @Override
    public UserPageData getProjectUserTableData(Long projectId) {
	if (ACCESS.getLevel(new ReadProjectPermission()) == ReadProjectPermission.LEVEL_NONE) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	List<User> resultList = JPA.createQuery(""
		+ "SELECT u "
		+ "  FROM " + User.class.getSimpleName() + " u "
		+ " JOIN FETCH u.projectAssignments pa "
		+ " WHERE KEY(pa).id = :projectId ",
		User.class)
		.setParameter("projectId", projectId)
		.getResultList();

	final UserPageData pageData = new UserPageData();
	final Project project = JPA.find(Project.class, projectId);
	FieldMapper.importTablePageData(resultList, pageData, (user, row) -> {
	    UserRowData userRow = (UserRowData) row;
	    userRow.setDisplayName(user.getDisplayName());
	    user.getProjectAssignments().entrySet().stream()
		    .filter(entry -> project.equals(entry.getKey()))
		    .map(Entry::getValue)
		    .findAny()
		    .ifPresent(userRow::setParticipationLevel);
	});

	return pageData;
    }

    @Override
    public UserPageData getAdministrationUserTableData() {
	if (!ACCESS.check(new ReadAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	List<User> result = JPA.createQuery("SELECT u FROM " + User.class.getSimpleName() + " u ", User.class)
		.getResultList();
	final UserPageData pageData = new UserPageData();

	FieldMapper.importTablePageData(result, pageData, (user, row) -> {
	    ((UserRowData) row).setDisplayName(user.getDisplayName());
	});

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
