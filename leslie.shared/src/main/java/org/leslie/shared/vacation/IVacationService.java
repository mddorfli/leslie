package org.leslie.shared.vacation;

import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.calendar.ICalendarItem;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

@TunnelToServer
public interface IVacationService extends IService {

    VacationTablePageData getVacationTableData(SearchFilter filter);

    VacationFormData prepareCreate(VacationFormData formData);

    VacationFormData create(VacationFormData formData);

    VacationFormData load(VacationFormData formData);

    VacationFormData store(VacationFormData formData);

    void approveVacation(Long vacationId);

    void remove(List<Long> activityIds);

    List<ICalendarItem> getCalendarItems(Date from, Date to);
}
