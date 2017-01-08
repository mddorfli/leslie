package org.leslie.shared.activity;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

@TunnelToServer
public interface IActivityService extends IService {

    ProjectActivityTablePageData getProjectActivityTableData(SearchFilter filter, Long projectId);
}
