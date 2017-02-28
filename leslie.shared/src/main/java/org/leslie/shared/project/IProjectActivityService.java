package org.leslie.shared.project;

import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.calendar.ICalendarItem;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.shared.activity.ProjectResourcesTablePageData;

@TunnelToServer
public interface IProjectActivityService extends IService {

    ProjectActivityFormData prepareCreate(ProjectActivityFormData formData);

    ProjectActivityFormData create(ProjectActivityFormData formData);

    ProjectActivityFormData load(ProjectActivityFormData formData);

    ProjectActivityFormData store(ProjectActivityFormData formData);

    void remove(List<Long> activityIds);

    List<ICalendarItem> getCurrentUserCalendarItems(Date from, Date to);

    ProjectResourcesTablePageData getProjectActivityTableData(SearchFilter filter, Long projectId);

    /**
     * In the header row:<br>
     * Column 1 is null.<br>
     * Columns 2 and onwards contain resource names.<br>
     * <br>
     * In the following rows:<br>
     * Column 1 is the date.<br>
     * Columns 2 and onwards contain the resource assignment percentage, or null
     * if unassigned.
     * 
     * @param projectId
     *            TODO
     * @param searchForm
     *            TODO
     * 
     * @return resource data.
     */
    Object[][] loadResourceData(Long projectId, ProjectResourcePlanSearchFormData searchForm);
}
