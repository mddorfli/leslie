package org.leslie.server.project;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.project.ProjectFormData;
import org.leslie.client.project.ProjectTablePageData;
import org.leslie.client.project.ProjectTablePageData.ProjectTableRowData;
import org.leslie.client.user.UserSelectionFormData;
import org.leslie.server.ServerSession;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Project;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.mapping.FieldMappingUtility;
import org.leslie.shared.code.ParticipationCodeType.Participation;
import org.leslie.shared.project.IProjectService;
import org.leslie.shared.security.permission.CreateProjectPermission;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.security.permission.ReadProjectPermission;

@Bean
public class ProjectService implements IProjectService {

    @Override
    public ProjectTablePageData getProjectTableData(SearchFilter filter) {
	int level = ACCESS.getLevel(new ReadProjectPermission());
	if (level == ReadProjectPermission.LEVEL_NONE) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	final ProjectTablePageData pageData = new ProjectTablePageData();
	TypedQuery<Project> query = JPA.createNamedQuery(Project.QUERY_ALL, Project.class);

	final User user = ServerSession.get().getUser();
	List<Project> resultList = query.getResultList().stream()
		.filter(project -> level == ReadProjectPermission.LEVEL_ALL
			|| level == ReadProjectPermission.LEVEL_PROJECT
				&& project.getUserAssignments().containsKey(user))
		.collect(Collectors.toList());

	FieldMappingUtility.importTablePageData(resultList, pageData, (project, row) -> {
	    if (project.getUserAssignments() != null) {
		project.getUserAssignments().entrySet().stream()
			.filter(entry -> user.equals(entry.getKey()))
			.map(Entry::getValue)
			.findAny()
			.ifPresent(parcipation -> ((ProjectTableRowData) row).setParticipation(parcipation));
	    }
	});

	return pageData;
    }

    @Override
    public ProjectFormData prepareCreate(ProjectFormData formData) {
	if (!ACCESS.check(new CreateProjectPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	return formData;
    }

    @Override
    public ProjectFormData create(ProjectFormData formData) {
	if (!ACCESS.check(new CreateProjectPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = new Project();
	FieldMappingUtility.exportFormData(formData, project);
	JPA.persist(project);
	return formData;
    }

    @Override
    public ProjectFormData load(ProjectFormData formData) {
	if (!ACCESS.check(new ReadProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, formData.getProjectId());
	FieldMappingUtility.importFormData(project, formData);
	return formData;
    }

    @Override
    public ProjectFormData store(ProjectFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, formData.getProjectId());
	FieldMappingUtility.exportFormData(formData, project);

	return formData;
    }

    @Override
    public void deleteProject(long projectId) {
	if (!ACCESS.check(new ManageProjectPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, projectId);
	JPA.remove(project);
    }

    @Override
    public void removeUser(long projectId, long userId) {
	if (!ACCESS.check(new ManageProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	User user = JPA.find(User.class, userId);
	Project project = JPA.find(Project.class, projectId);
	project.getUserAssignments().remove(user);
    }

    @Override
    public void assignUser(UserSelectionFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, formData.getProjectId());
	User user = JPA.find(User.class, formData.getUser().getValue());

	project.getUserAssignments().put(user, formData.getParticiaption().getValue());
    }

    @Override
    public Participation getParticipationLevel(long projectId) {
	Project project = JPA.find(Project.class, projectId);
	return project.getUserAssignments().get(ServerSession.get().getUser());
    }
}
