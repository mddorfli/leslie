package org.leslie.server.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Activity;
import org.leslie.server.jpa.entity.ProjectActivity;
import org.leslie.server.jpa.entity.VacationActivity;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class ActivityServiceTest {

    @Test
    public void testQueries() {
	Activity activity = JPA.createNamedQuery(Activity.QUERY_BY_IDS, Activity.class)
		.setParameter("activityIds", Collections.singletonList(Long.valueOf(1L)))
		.getSingleResult();
	assertTrue(activity instanceof ProjectActivity);
	ProjectActivity pa = (ProjectActivity) activity;
	assertEquals(1L, pa.getId());
	assertEquals("mdo", pa.getUser().getUsername());
	assertEquals(80.0, pa.getPercentage(), 0.1);
    }

    @Test
    public void testLoadProjectActivity() {
	ProjectActivityFormData formData = new ProjectActivityFormData();
	formData.setActivityId(1L);
	BEANS.get(IProjectActivityService.class).load(formData);

	assertEquals(Long.valueOf(2L), formData.getUser().getValue());
	assertEquals(80.0, formData.getPercentage().getValue().doubleValue(), 0.1);
    }

    @Test
    public void testVerifyDateOverlap() {
	Date fromDate = Date.from(LocalDate.of(2017, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
	Date toDate = Date.from(LocalDate.of(2017, 03, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
	assertFalse(AbstractActivityService.getCollisions(Activity.class, fromDate, toDate,
		Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()).isEmpty());
	assertFalse(AbstractActivityService.getCollisions(Activity.class, fromDate, toDate,
		Optional.of(Long.valueOf(2L)), Optional.of(ProjectActivity.class), Optional.empty(), Optional.empty())
		.isEmpty());
	assertTrue(AbstractActivityService.getCollisions(Activity.class, fromDate, toDate,
		Optional.of(Long.valueOf(2L)), Optional.of(VacationActivity.class), Optional.empty(), Optional.empty())
		.isEmpty());
	assertTrue(AbstractActivityService.getCollisions(Activity.class, fromDate, toDate,
		Optional.of(Long.valueOf(5L)), Optional.of(ProjectActivity.class), Optional.empty(), Optional.empty())
		.isEmpty());
	assertTrue(AbstractActivityService.getCollisions(Activity.class, fromDate, toDate,
		Optional.of(Long.valueOf(3L)), Optional.of(ProjectActivity.class),
		Optional.of(Long.valueOf(3L)), Optional.empty()).isEmpty());

	fromDate = Date.from(LocalDate.of(2017, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
	toDate = Date.from(LocalDate.of(2017, 04, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
	assertFalse(AbstractActivityService.getCollisions(Activity.class, fromDate, toDate,
		Optional.of(Long.valueOf(2L)), Optional.of(VacationActivity.class), Optional.empty(), Optional.empty())
		.isEmpty());
	assertTrue(AbstractActivityService.getCollisions(Activity.class, fromDate, toDate,
		Optional.of(Long.valueOf(2L)), Optional.of(VacationActivity.class), Optional.of(5L), Optional.empty())
		.isEmpty());
    }

    @Test
    public void readUserActivityTest() {
	List<Activity> resultList = JPA.createQuery(""
		+ "SELECT pa "
		+ "  FROM Activity pa "
		+ "  JOIN pa.user pu "
		+ " WHERE pu.username = 'mdo' "
		+ "   AND TYPE(pa) = ProjectActivity ",
		Activity.class).getResultList();

	double percentage = 0.0;
	for (Activity activity : resultList) {
	    Date startTime = Date.from(LocalDate.of(2017, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    Date endTime = Date.from(LocalDate.of(2017, 03, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    assertEquals(startTime, activity.getFrom());
	    assertEquals(endTime, activity.getTo());

	    assertTrue(activity instanceof ProjectActivity);
	    ProjectActivity pa = (ProjectActivity) activity;
	    percentage += pa.getPercentage();
	}
	assertEquals("Project bookings for mdo should add up to 100%", 100.0, percentage, 0.1);
    }
}
