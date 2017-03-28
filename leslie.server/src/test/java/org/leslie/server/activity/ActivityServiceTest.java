package org.leslie.server.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.server.entity.Activity;
import org.leslie.server.entity.ProjectActivity;
import org.leslie.server.entity.VacationActivity;
import org.leslie.server.jpa.JPA;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class ActivityServiceTest {

	@Test
	public void testQueries() {
		Activity activity = JPA.createNamedQuery(Activity.QUERY_BY_IDS, Activity.class)
				.setParameter("activityIds", Collections.singletonList(Long.valueOf(1L))).getSingleResult();
		assertTrue(activity instanceof ProjectActivity);
		ProjectActivity pa = (ProjectActivity) activity;
		assertEquals(1L, pa.getId());
		assertEquals("mdo", pa.getUser().getUsername());
		assertEquals(0.8, pa.getPercentage(), 0.01);
	}

	@Test
	public void testLoadProjectActivity() {
		ProjectActivityFormData formData = new ProjectActivityFormData();
		formData.setActivityId(1L);
		BEANS.get(IProjectActivityService.class).load(formData);

		assertEquals(Long.valueOf(2L), formData.getUser().getValue());
		assertEquals(0.8, formData.getPercentage().getValue().doubleValue(), 0.01);
	}

	@Test
	public void testVerifyDateOverlap() {
		Date fromDate = Date.from(LocalDate.of(2017, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date toDate = Date.from(LocalDate.of(2017, 03, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
		assertFalse(ActivityService.getCollisions(Activity.class, fromDate, toDate, null, null).isEmpty());
		assertFalse(ActivityService.getCollisions(ProjectActivity.class, fromDate, toDate, Long.valueOf(2L), null).isEmpty());
		assertTrue(ActivityService.getCollisions(VacationActivity.class, fromDate, toDate, Long.valueOf(2L), null).isEmpty());
		assertTrue(ActivityService.getCollisions(Activity.class, fromDate, toDate, Long.valueOf(5L), null).isEmpty());
		assertTrue(ActivityService.getCollisions(Activity.class, fromDate, toDate, Long.valueOf(3L), Long.valueOf(3L))
				.isEmpty());

		fromDate = Date.from(LocalDate.of(2017, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
		toDate = Date.from(LocalDate.of(2017, 04, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
		assertFalse(ActivityService.getCollisions(Activity.class, fromDate, toDate, Long.valueOf(2L), null).isEmpty());
		assertTrue(ActivityService.getCollisions(VacationActivity.class, fromDate, toDate, Long.valueOf(2L), 5L).isEmpty());
	}

	@Test
	public void readUserActivityTest() {
		List<Activity> resultList = JPA
				.createQuery("" + "SELECT pa " + "  FROM Activity pa " + "  JOIN pa.user pu "
						+ " WHERE pu.username = 'mdo' " + "   AND TYPE(pa) = ProjectActivity ", Activity.class)
				.getResultList();

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
		assertEquals("Project bookings for mdo should add up to 1.0", 1.0, percentage, 0.01);
	}
}
