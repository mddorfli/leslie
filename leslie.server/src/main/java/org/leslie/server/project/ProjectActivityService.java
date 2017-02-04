package org.leslie.server.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.VetoException;
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
import org.leslie.server.jpa.entity.ProjectActivity;
import org.leslie.server.jpa.mapping.FieldMappingUtility;
import org.leslie.shared.activity.ProjectActivityTablePageData;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.security.permission.ReadProjectPermission;

@Bean
public class ProjectActivityService implements IProjectActivityService {

    @Override
    public ProjectActivityTablePageData getProjectActivityTableData(SearchFilter filter, Long projectId) {
	if (!ACCESS.check(new ReadProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	List<ProjectActivity> resultList = JPA
		.createNamedQuery(ProjectActivity.QUERY_BY_PROJECTID, ProjectActivity.class)
		.setParameter("projectId", projectId)
		.getResultList();

	ProjectActivityTablePageData pageData = new ProjectActivityTablePageData();
	FieldMappingUtility.importTablePageData(resultList, pageData);

	return pageData;
    }

    @Override
    public ProjectActivityFormData prepareCreate(ProjectActivityFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	return formData;
    }

    @Override
    public ProjectActivityFormData create(ProjectActivityFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	if (ActivityService.getCollisions(ProjectActivity.class,
		formData.getFrom().getValue(), formData.getTo().getValue(),
		formData.getUser().getValue(), null).stream()
		.anyMatch(pa -> pa.getProject().getId() == formData.getProjectId().longValue())) {
	    throw new VetoException(TEXTS.get("ProjectBookingOverlaps"));
	}

	ProjectActivity pa = new ProjectActivity();
	FieldMappingUtility.exportFormData(formData, pa);
	JPA.persist(pa);

	return formData;
    }

    @Override
    public ProjectActivityFormData load(ProjectActivityFormData formData) {
	ProjectActivity pa = JPA.find(ProjectActivity.class, formData.getActivityId());
	if (!ACCESS.check(new ReadProjectPermission(pa.getProject().getId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	FieldMappingUtility.importFormData(pa, formData);
	return formData;
    }

    @Override
    public ProjectActivityFormData store(ProjectActivityFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	if (ActivityService.getCollisions(ProjectActivity.class,
		formData.getFrom().getValue(), formData.getTo().getValue(),
		formData.getUser().getValue(), formData.getActivityId()).stream()
		.anyMatch(pa -> pa.getProject().getId() == formData.getProjectId().longValue())) {
	    throw new VetoException(TEXTS.get("ProjectBookingOverlaps"));
	}

	ProjectActivity pa = JPA.find(ProjectActivity.class, formData.getActivityId());
	FieldMappingUtility.exportFormData(formData, pa);
	return formData;
    }

    @Override
    public void remove(List<Long> activityIds) {
	ActivityService.remove(activityIds);
    }

    @Override
    public List<ICalendarItem> getCurrentUserCalendarItems(Date from, Date to) {
	return new ArrayList<>(JPA.createNamedQuery(Activity.QUERY_BY_USERID_TYPE_FROM_TO, ProjectActivity.class)
		.setParameter("type", ProjectActivity.class)
		.setParameter("userId", ServerSession.get().getUserNr())
		.setParameter("from", from)
		.setParameter("to", to)
		.getResultList().stream()
		.map(pa -> {
		    // increment end date by 1 so it displays correctly
		    return new CalendarAppointment(pa.getId(), pa.getUser().getDisplayName(),
			    pa.getFrom(), DateUtility.addDays(pa.getTo(), 1), true, pa.getProject().getName(),
			    String.format("%.0f%%", pa.getPercentage()), null);
		}).collect(Collectors.toList()));
    }
}
