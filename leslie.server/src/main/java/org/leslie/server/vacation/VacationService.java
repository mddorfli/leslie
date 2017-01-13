package org.leslie.server.vacation;

import java.util.List;

import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.server.ServerSession;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.VacationActivity;
import org.leslie.server.jpa.mapping.FieldMappingUtility;
import org.leslie.shared.vacation.IVacationService;
import org.leslie.shared.vacation.VacationTablePageData;

public class VacationService implements IVacationService {

    @Override
    public VacationTablePageData getVacationTableData(SearchFilter filter) {
	VacationTablePageData pageData = new VacationTablePageData();
	List<VacationActivity> vacations = JPA.createNamedQuery(VacationActivity.QUERY_BY_USER, VacationActivity.class)
		.setParameter("user", ServerSession.get().getUser())
		.getResultList();
	FieldMappingUtility.importTablePageData(vacations, pageData);
	return pageData;
    }
}
