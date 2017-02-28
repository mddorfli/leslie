package org.leslie.server.user;

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
import org.leslie.server.ServerSession;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Activity;
import org.leslie.server.jpa.entity.Project;
import org.leslie.server.jpa.entity.ProjectAssignment;
import org.leslie.server.jpa.entity.Role;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.mapping.FieldMappingUtility;
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
	FieldMappingUtility.exportFormData(formData, user);
	applyPassword(user, formData.getPassword().getValue());

	JPA.persist(user);
	return formData;
    }

    @Override
    public UserFormData load(UserFormData formData) throws ProcessingException {
	if (!ACCESS.check(new ReadAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	User user = JPA.find(User.class, formData.getUserId());
	FieldMappingUtility.importFormData(user, formData);
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
	User user = JPA.find(User.class, formData.getUserId());
	FieldMappingUtility.exportFormData(formData, user);

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
	return formData;
    }

    @Override
    public void delete(Long userId) throws ProcessingException {
	if (!ACCESS.check(new UpdateAdministrationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	if (ServerSession.get().getUserNr().equals(userId)) {
	    throw new VetoException(TEXTS.get("YouCantDeleteYourself"));
	}
	User user = JPA.find(User.class, userId);
	List<Activity> activities = JPA
		.createQuery("SELECT a FROM Activity a WHERE a.user.id = :userId ", Activity.class)
		.setParameter("userId", userId)
		.getResultList();
	activities.forEach(JPA::remove);
	user.getProjectAssignments().forEach(JPA::remove);
	user.getProjectAssignments().clear();
	user.getRoles().clear();
	JPA.remove(user);
    }

    @Override
    public UserPageData getProjectUserTableData(Long projectId) {
	if (ACCESS.getLevel(new ReadProjectPermission()) == ReadProjectPermission.LEVEL_NONE) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	final Project project = JPA.find(Project.class, projectId);
	List<User> users = JPA.createNamedQuery(User.QUERY_BY_PROJECT_ID, User.class)
		.setParameter("projectId", projectId)
		.getResultList();

	final UserPageData pageData = new UserPageData();
	FieldMappingUtility.importTablePageData(users, pageData, (user, row) -> {
	    UserRowData userRow = (UserRowData) row;
	    userRow.setDisplayName(user.getDisplayName());
	    for (ProjectAssignment pa : project.getUserAssignments()) {
		if (user.equals(pa.getUser())) {
		    userRow.setParticipationLevel(pa.getParticipationLevel());
		    break;
		}
	    }
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

	FieldMappingUtility.importTablePageData(result, pageData, (user, row) -> {
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

    @Override
    public Long getUserId(String username) {
	return JPA.createNamedQuery(User.QUERY_BY_USERNAME, User.class)
		.setParameter("username", username)
		.getSingleResult()
		.getId();
    }

    @Override
    public Long getCurrentUserNr() {
	return ServerSession.get().getUserNr();
    }
}
