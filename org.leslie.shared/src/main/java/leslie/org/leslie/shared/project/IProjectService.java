package leslie.org.leslie.shared.project;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import leslie.org.leslie.shared.user.UserSelectionFormData;

@TunnelToServer
public interface IProjectService extends IService {

	ProjectTablePageData getProjectTableData(SearchFilter filter);

	ProjectFormData prepareCreate(ProjectFormData formData);

	ProjectFormData create(ProjectFormData formData);

	ProjectFormData load(ProjectFormData formData);

	ProjectFormData store(ProjectFormData formData);

	void deleteProject(Long projectId);

	void removeUser(Long projectId, Long userId);

	void assignUser(UserSelectionFormData formData);
}