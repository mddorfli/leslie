package org.leslie.server.user;

import java.util.ArrayList;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Bean
public class UserService implements IUserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

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
	    List<Role> newRoles = JPA.createNamedQuery(Role.QUERY_BY_IDS, Role.class)
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

	final Project project = JPA.find(Project.class, projectId);
	List<User> users = new ArrayList<>(project.getUserAssignments().keySet());

	final UserPageData pageData = new UserPageData();
	FieldMapper.importTablePageData(users, pageData, (user, row) -> {
	    UserRowData userRow = (UserRowData) row;
	    userRow.setDisplayName(user.getDisplayName());
	    userRow.setParticipationLevel(project.getUserAssignments().get(user));
	});

	return pageData;
    }

    @Override
    public UserPageData getAdministrationUserTableData() {
	if (!ACCESS.check(new ReadAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	List<User> result = JPA.createNamedQuery(User.QUERY_ALL, User.class).getResultList();
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
}
