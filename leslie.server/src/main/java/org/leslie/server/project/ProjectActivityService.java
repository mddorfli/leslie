package org.leslie.server.project;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.mapping.FieldMappingUtility;
import org.leslie.shared.activity.ProjectResourcesTablePageData;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;
import org.leslie.shared.project.ProjectResourcePlanSearchFormData;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.security.permission.ReadProjectPermission;

@Bean
public class ProjectActivityService implements IProjectActivityService {

    @Override
    public ProjectResourcesTablePageData getProjectActivityTableData(SearchFilter filter, Long projectId) {
	if (!ACCESS.check(new ReadProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	List<ProjectActivity> resultList = JPA
		.createNamedQuery(ProjectActivity.QUERY_BY_PROJECTID_FETCH_USER_PROJECT, ProjectActivity.class)
		.setParameter("projectId", projectId)
		.getResultList();

	ProjectResourcesTablePageData pageData = new ProjectResourcesTablePageData();
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

    @Override
    public Object[][] loadResourceData(Long projectId, ProjectResourcePlanSearchFormData searchForm) {
	if (!ACCESS.check(new ReadProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Date searchFrom = searchForm.getFrom().getValue();
	Date searchTo = searchForm.getTo().getValue();

	CriteriaBuilder cb = JPA.getCriteriaBuilder();
	CriteriaQuery<ProjectActivity> q = cb.createQuery(ProjectActivity.class);
	Root<ProjectActivity> pa = q.from(ProjectActivity.class);
	pa.fetch("user");
	q.select(pa);
	q.where(cb.equal(pa.get("project").get("id"), cb.parameter(String.class, "projectId")));
	q.orderBy(cb.desc(pa.get("from")), cb.desc(pa.get("user").get("id")));
	if (searchFrom != null) {
	    q.where(cb.greaterThanOrEqualTo(pa.get("from"), cb.parameter(Date.class, "from")));
	}
	if (searchTo != null) {
	    q.where(cb.lessThan(pa.get("to"), cb.parameter(Date.class, "to")));
	}
	TypedQuery<ProjectActivity> query = JPA.createQuery(q);

	query.setParameter("projectId", projectId);
	if (searchForm != null) {
	    query.setParameter("from", searchFrom, TemporalType.DATE);
	}
	if (searchTo != null) {
	    query.setParameter("to", searchTo, TemporalType.DATE);
	}

	SortedMap<LocalDate, HashMap<User, Double>> data = new TreeMap<>();
	SortedSet<User> users = new TreeSet<>((a, b) -> {
	    return a.getDisplayName().compareTo(b.getDisplayName());
	});
	for (ProjectActivity entry : query.getResultList()) {
	    LocalDate to = LocalDateTime.ofInstant(entry.getTo().toInstant(), ZoneId.systemDefault()).toLocalDate();
	    LocalDate from = LocalDateTime.ofInstant(entry.getFrom().toInstant(), ZoneId.systemDefault()).toLocalDate();
	    assert from.isBefore(to);
	    users.add(entry.getUser());
	    for (LocalDate day = from; !day.isAfter(to); day = day.plusDays(1)) {
		if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY) {
		    continue;
		}
		HashMap<User, Double> dayData = data.getOrDefault(day, new HashMap<>());
		dayData.put(entry.getUser(), entry.getPercentage());
		data.putIfAbsent(day, dayData);
	    }
	}

	List<Object[]> rowData = new ArrayList<>();

	// add the header row (first cell is empty)
	User[] userColumns = users.toArray(new User[users.size()]);
	String[] headerRow = new String[userColumns.length + 1];
	for (int i = 0; i < userColumns.length; i++) {
	    headerRow[i + 1] = userColumns[i].getDisplayName();
	}
	rowData.add(headerRow);

	// collate the data rows
	for (Entry<LocalDate, HashMap<User, Double>> dayData : data.entrySet()) {
	    Object[] row = new Object[userColumns.length + 1];
	    row[0] = Date.from(dayData.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant());
	    HashMap<User, Double> value = dayData.getValue();
	    for (int i = 0; i < userColumns.length; i++) {
		row[i + 1] = value.get(userColumns[i]);
	    }
	    rowData.add(row);
	}

	return rowData.toArray(new Object[rowData.size()][]);
    }
}
