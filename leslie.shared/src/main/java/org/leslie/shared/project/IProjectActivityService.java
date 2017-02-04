package org.leslie.shared.project;

import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.calendar.ICalendarItem;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.shared.activity.ProjectActivityTablePageData;

@TunnelToServer
public interface IProjectActivityService extends IService {

    ProjectActivityTablePageData getProjectActivityTableData(SearchFilter filter, Long projectId);

    ProjectActivityFormData prepareCreate(ProjectActivityFormData formData);

    ProjectActivityFormData create(ProjectActivityFormData formData);

    ProjectActivityFormData load(ProjectActivityFormData formData);

    ProjectActivityFormData store(ProjectActivityFormData formData);

    void remove(List<Long> activityIds);

    List<ICalendarItem> getCurrentUserCalendarItems(Date from, Date to);

}
