package org.leslie.shared.project;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IProjectActivityService extends IService {

    ProjectActivityFormData prepareCreate(ProjectActivityFormData formData);

    ProjectActivityFormData create(ProjectActivityFormData formData);

    ProjectActivityFormData load(ProjectActivityFormData formData);

    ProjectActivityFormData store(ProjectActivityFormData formData);

}
