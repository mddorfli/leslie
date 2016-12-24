package org.leslie.shared.project;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.project.ProjectFormData;
import org.leslie.client.project.ProjectTablePageData;
import org.leslie.client.user.UserSelectionFormData;

@TunnelToServer
public interface IProjectService extends IService {

    ProjectTablePageData getProjectTableData(SearchFilter filter);

    ProjectFormData prepareCreate(ProjectFormData formData);

    ProjectFormData create(ProjectFormData formData);

    ProjectFormData load(ProjectFormData formData);

    ProjectFormData store(ProjectFormData formData);

    void deleteProject(long projectId);

    void removeUser(long projectId, long userId);

    void assignUser(UserSelectionFormData formData);

    boolean isOwnProject(long projectId);
}
