package org.leslie.server.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

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
}
