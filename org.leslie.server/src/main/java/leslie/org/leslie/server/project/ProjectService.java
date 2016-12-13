package leslie.org.leslie.server.project;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.Project;
import org.leslie.server.jpa.User;
import org.leslie.server.jpa.UserProjectAssignment;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.project.IProjectService;
import leslie.org.leslie.shared.project.ProjectFormData;
import leslie.org.leslie.shared.project.ProjectTablePageData;
import leslie.org.leslie.shared.project.ProjectTablePageData.ProjectTableRowData;
import leslie.org.leslie.shared.security.permission.CreateProjectPermission;
import leslie.org.leslie.shared.security.permission.ReadProjectPermission;
import leslie.org.leslie.shared.security.permission.UpdateProjectPermission;
import leslie.org.leslie.shared.user.UserSelectionFormData;

@Bean
public class ProjectService implements IProjectService {

    @Override
    public ProjectTablePageData getProjectTableData(SearchFilter filter) {
	// if (!ACCESS.check(new ReadProjectPermission())) {
	// throw new VetoException(TEXTS.get("AuthorizationFailed"));
	// }
	final ProjectTablePageData pageData = new ProjectTablePageData();
	JPA.createQuery(""
		+ "SELECT p "
		+ "FROM " + Project.class.getSimpleName() + " p ",
		// TODO Add security filter to select statement
		Project.class)
		.getResultList().stream()
		.forEach(project -> importRowData(pageData.addRow(), project));
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
	exportFormData(formData, project);
	JPA.persist(project);
	return formData;
    }

    @Override
    public ProjectFormData load(ProjectFormData formData) {
	if (!ACCESS.check(new ReadProjectPermission(formData.getId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, formData.getId());
	importFormData(formData, project);
	return formData;
    }

    @Override
    public ProjectFormData store(ProjectFormData formData) {
	if (!ACCESS.check(new UpdateProjectPermission(formData.getId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, formData.getId());
	exportFormData(formData, project);
	JPA.merge(project);

	return formData;
    }

    @Override
    public void deleteProject(Long projectId) {
	if (!ACCESS.check(new UpdateProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, projectId);
	JPA.remove(project);
    }

    @Override
    public void removeUser(Long projectId, Long userId) {
	if (!ACCESS.check(new UpdateProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	User user = JPA.find(User.class, userId);
	user.getProjectAssignments().removeIf(assignment -> assignment.getProject().getId() == projectId);
	JPA.merge(user);
    }

    @Override
    public void assignUser(UserSelectionFormData formData) {
	if (!ACCESS.check(new UpdateProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Project project = JPA.find(Project.class, formData.getProjectId());
	User user = JPA.find(User.class, formData.getUser().getValue());
	UserProjectAssignment assignment = new UserProjectAssignment();
	assignment.setProject(project);
	assignment.setUser(user);
	// TODO assign role also
	user.getProjectAssignments().add(assignment);
	JPA.persist(assignment);
    }

    private static void importRowData(ProjectTableRowData row, Project project) {
	row.setId(project.getId());
	row.setName(project.getName());
	row.setVersion(project.getVersion());
    }

    private static void exportFormData(ProjectFormData formData, Project project) {
	project.setName(formData.getName().getValue());
	project.setVersion(formData.getVersion().getValue());
    }

    private static void importFormData(ProjectFormData formData, Project project) {
	formData.getName().setValue(project.getName());
	formData.getVersion().setValue(project.getVersion());
    }
}
