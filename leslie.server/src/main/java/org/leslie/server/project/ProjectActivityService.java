package org.leslie.server.project;

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
import org.leslie.server.entity.Activity;
import org.leslie.server.entity.ProjectActivity;
import org.leslie.server.entity.User;
import org.leslie.server.jpa.JPA;
import org.leslie.server.mapping.MappingUtility;
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
	MappingUtility.importTablePageData(resultList, pageData);

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
	MappingUtility.exportFormData(formData, pa);
	JPA.persist(pa);

	return formData;
    }

    @Override
    public ProjectActivityFormData load(ProjectActivityFormData formData) {
	ProjectActivity pa = JPA.find(ProjectActivity.class, formData.getActivityId());
	if (!ACCESS.check(new ReadProjectPermission(pa.getProject().getId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	MappingUtility.importFormData(pa, formData);
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
	MappingUtility.exportFormData(formData, pa);
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
			    String.format("%.0f%%", pa.getPercentage()*100.0), null);
		}).collect(Collectors.toList()));
    }

    @Override
    public Object[][] loadResourceData(Long projectId, ProjectResourcePlanSearchFormData searchForm) {
	if (!ACCESS.check(new ReadProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	Date searchFrom = DateUtility.truncDate(searchForm.getFrom().getValue());
	Date searchTo = DateUtility.truncDate(searchForm.getTo().getValue());

	List<ProjectActivity> resultList = JPA.createNamedQuery(
		ProjectActivity.QUERY_BY_PROJECTID_FETCH_USER_SORTED, ProjectActivity.class)
		.setParameter("projectId", projectId)
		.getResultList();

	SortedMap<Date, HashMap<User, Double>> data = new TreeMap<>();
	SortedSet<User> users = new TreeSet<>((a, b) -> {
	    return a.getDisplayName().compareTo(b.getDisplayName());
	});
	for (ProjectActivity entry : resultList) {
	    Date from = DateUtility.truncDate(entry.getFrom());
	    Date to = DateUtility.truncDate(entry.getTo());
	    assert from.before(to);
	    if (!users.contains(entry.getUser())) {
		users.add(entry.getUser());
	    }
	    for (Date day = from; !day.after(to); day = DateUtility.addDays(day, 1)) {
		// filtering
		if (DateUtility.isWeekend(day)
			|| searchFrom != null && day.before(searchFrom)
			|| searchTo != null && day.after(searchTo)) {
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
	String[] headerRow = new String[userColumns.length + 2];
	for (int i = 0; i < userColumns.length; i++) {
	    headerRow[i + 2] = userColumns[i].getDisplayName();
	}
	rowData.add(headerRow);

	// collate the data rows
	for (Entry<Date, HashMap<User, Double>> dayData : data.entrySet()) {
	    Object[] row = new Object[userColumns.length + 2];
	    row[0] = dayData.getKey();
	    HashMap<User, Double> value = dayData.getValue();
	    double total = 0.0;
	    for (int i = 0; i < userColumns.length; i++) {
		Double resourcePercent = value.get(userColumns[i]);
		row[i + 2] = resourcePercent;
		if (resourcePercent != null) {
		    total += resourcePercent.doubleValue();
		}
	    }
	    row[1] = total;
	    rowData.add(row);
	}

	return rowData.toArray(new Object[rowData.size()][]);
    }
}
