package org.leslie.shared.activity;

import java.util.List;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

@TunnelToServer
public interface IActivityService extends IService {

    ProjectActivityTablePageData getProjectActivityTableData(SearchFilter filter, Long projectId);

    void remove(List<Long> activityIds);
}
