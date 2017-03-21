package org.leslie.server.vacation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.calendar.CalendarAppointment;
import org.eclipse.scout.rt.shared.services.common.calendar.ICalendarItem;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.ServerSession;
import org.leslie.server.activity.ActivityService;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Activity;
import org.leslie.server.jpa.entity.VacationActivity;
import org.leslie.server.jpa.mapping.MappingUtility;
import org.leslie.shared.security.permission.ApproveVacationPermission;
import org.leslie.shared.security.permission.RequestVacationPermission;
import org.leslie.shared.vacation.IVacationService;
import org.leslie.shared.vacation.VacationFormData;
import org.leslie.shared.vacation.VacationTablePageData;

@Bean
public class VacationService implements IVacationService {

    @Override
    public VacationTablePageData getVacationTableData(SearchFilter filter) {
	VacationTablePageData pageData = new VacationTablePageData();
	final List<VacationActivity> vacations;
	if (ACCESS.check(new ApproveVacationPermission())) {
	    vacations = JPA.createNamedQuery(VacationActivity.QUERY_ALL, VacationActivity.class)
		    .getResultList();
	} else {
	    vacations = JPA.createNamedQuery(VacationActivity.QUERY_BY_USER, VacationActivity.class)
		    .setParameter("user", ServerSession.get().getUser())
		    .getResultList();
	}
	MappingUtility.importTablePageData(vacations, pageData);
	return pageData;
    }

    @Override
    public VacationFormData prepareCreate(VacationFormData formData) {
	if (!ACCESS.check(new RequestVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	formData.getRequestedBy().setValue(ServerSession.get().getUserNr());
	return formData;
    }

    @Override
    public VacationFormData create(VacationFormData formData) {
	if (!ACCESS.check(new RequestVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	if (!ActivityService.getCollisions(VacationActivity.class,
		formData.getFrom().getValue(), formData.getTo().getValue(),
		formData.getRequestedBy().getValue(), null).isEmpty()) {
	    throw new VetoException(TEXTS.get("VacationOverlaps"));
	}
	VacationActivity va = new VacationActivity();
	MappingUtility.exportFormData(formData, va);
	JPA.persist(va);
	return formData;
    }

    @Override
    public VacationFormData load(VacationFormData formData) {
	VacationActivity va = JPA.find(VacationActivity.class, formData.getActivityId());
	// you can only update your own vacations, unless you have the approve
	// vacation permission
	if (!(va.getUser().getId() == ServerSession.get().getUserNr().longValue()
		|| ACCESS.check(new ApproveVacationPermission()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	MappingUtility.importFormData(va, formData);
	return formData;
    }

    @Override
    public VacationFormData store(VacationFormData formData) {
	if ((formData.getApprovedBy().getValue() != null
		|| !ServerSession.get().getUserNr().equals(formData.getRequestedBy().getValue()))
		&& !ACCESS.check(new ApproveVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	if (!ActivityService.getCollisions(VacationActivity.class,
		formData.getFrom().getValue(), formData.getTo().getValue(),
		formData.getRequestedBy().getValue(), formData.getActivityId()).isEmpty()) {
	    throw new VetoException(TEXTS.get("VacationOverlaps"));
	}
	VacationActivity va = JPA.find(VacationActivity.class, formData.getActivityId());
	MappingUtility.exportFormData(formData, va);
	return formData;
    }

    @Override
    public void approveVacation(Long vacationId) {
	if (!ACCESS.check(new ApproveVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	JPA.find(VacationActivity.class, vacationId)
		.setApprovedBy(ServerSession.get().getUser());
    }

    @Override
    public void remove(List<Long> activityIds) {
	ActivityService.remove(activityIds);
    }

    @Override
    public List<ICalendarItem> getCalendarItems(Date from, Date to) {
	Long userId = ServerSession.get().getUserNr();
	return new ArrayList<>(JPA.createNamedQuery(Activity.QUERY_BY_USERID_TYPE_FROM_TO, VacationActivity.class)
		.setParameter("type", VacationActivity.class)
		.setParameter("userId", userId)
		.setParameter("from", from)
		.setParameter("to", to)
		.getResultList().stream()
		.map(va -> {
		    String vacationText = TEXTS.get("Vacation")
			    + (StringUtility.isNullOrEmpty(va.getDescription()) ? "" : " - " + va.getDescription());
		    // increment end date by 1 so it displays correctly
		    return new CalendarAppointment(va.getId(), va.getUser().getDisplayName(),
			    va.getFrom(), DateUtility.addDays(va.getTo(), 1), true, vacationText,
			    (va.getApprovedBy() == null ? TEXTS.get("PendingApproval") : TEXTS.get("Approved")),
			    null);
		})
		.collect(Collectors.toList()));
    }
}
