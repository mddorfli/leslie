package org.leslie.shared.vacation;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

@TunnelToServer
public interface IVacationService extends IService {

    VacationTablePageData getVacationTableData(SearchFilter filter);
}
