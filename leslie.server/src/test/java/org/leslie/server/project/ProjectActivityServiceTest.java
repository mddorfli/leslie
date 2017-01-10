package org.leslie.server.project;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class ProjectActivityServiceTest {

    @Test
    public void testLoad() {
	ProjectActivityFormData formData = new ProjectActivityFormData();
	formData.setActivityId(Long.valueOf(1L));
	BEANS.get(IProjectActivityService.class).load(formData);
    }

}
