package leslie.org.leslie.server.project;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.StoredProject;
import org.leslie.server.jpa.StoredUser;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.project.IProjectService;
import leslie.org.leslie.shared.project.ProjectFormData;
import leslie.org.leslie.shared.project.ProjectTablePageData;
import leslie.org.leslie.shared.project.ProjectTablePageData.ProjectTableRowData;
import leslie.org.leslie.shared.security.CreateProjectPermission;
import leslie.org.leslie.shared.security.ReadProjectPermission;
import leslie.org.leslie.shared.security.UpdateProjectPermission;
import leslie.org.leslie.shared.user.UserSelectionFormData;

@Bean
public class ProjectService implements IProjectService {

	@Override
	public ProjectTablePageData getProjectTableData(SearchFilter filter) {
		if (!ACCESS.check(new ReadProjectPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		final ProjectTablePageData pageData = new ProjectTablePageData();
		JPA.createQuery(""
				+ "SELECT p "
				+ "FROM " + StoredProject.class.getSimpleName() + " p ",
				StoredProject.class)
				.getResultList()
				.stream()
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
		StoredProject project = new StoredProject();
		exportFormData(formData, project);
		JPA.persist(project);
		return formData;
	}

	@Override
	public ProjectFormData load(ProjectFormData formData) {
		if (!ACCESS.check(new ReadProjectPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredProject project = JPA.find(StoredProject.class, formData.getId());
		importFormData(formData, project);
		return formData;
	}

	@Override
	public ProjectFormData store(ProjectFormData formData) {
		if (!ACCESS.check(new UpdateProjectPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredProject project = JPA.find(StoredProject.class, formData.getId());
		exportFormData(formData, project);
		JPA.merge(project);

		return formData;
	}

	@Override
	public void deleteProject(Long projectId) {
		if (!ACCESS.check(new UpdateProjectPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredProject project = JPA.find(StoredProject.class, projectId);
		JPA.remove(project);
	}

	@Override
	public void removeUser(Long projectId, Long userId) {
		if (!ACCESS.check(new UpdateProjectPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = JPA.find(StoredUser.class, userId);
		user.getProjects().removeIf(project -> project.getId() == projectId);
		JPA.merge(user);
	}

	@Override
	public void assignUser(UserSelectionFormData formData) {
		if (!ACCESS.check(new UpdateProjectPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredProject project = JPA.find(StoredProject.class, formData.getProjectId());
		StoredUser user = JPA.find(StoredUser.class, formData.getUser().getValue());
		user.getProjects().add(project);
		JPA.merge(user);
	}

	private static void importRowData(ProjectTableRowData row, StoredProject project) {
		row.setId(project.getId());
		row.setName(project.getName());
		row.setVersion(project.getVersion());
	}

	private static void exportFormData(ProjectFormData formData, StoredProject project) {
		project.setName(formData.getName().getValue());
		project.setVersion(formData.getVersion().getValue());
	}

	private static void importFormData(ProjectFormData formData, StoredProject project) {
		formData.getName().setValue(project.getName());
		formData.getVersion().setValue(project.getVersion());
	}
}
