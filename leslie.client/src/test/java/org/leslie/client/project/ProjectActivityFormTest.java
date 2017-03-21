package org.leslie.client.project;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.mock.BeanMock;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;
import org.mockito.Matchers;
import org.mockito.Mockito;

@RunWithSubject("anonymous")
@RunWith(ClientTestRunner.class)
@RunWithClientSession(TestEnvironmentClientSession.class)
public class ProjectActivityFormTest {

    @BeanMock
    private IProjectActivityService m_mockSvc;

    @Before
    public void setup() {
	ProjectActivityFormData answer = new ProjectActivityFormData();
	Mockito.when(m_mockSvc.prepareCreate(Matchers.any(ProjectActivityFormData.class))).thenReturn(answer);
	Mockito.when(m_mockSvc.create(Matchers.any(ProjectActivityFormData.class))).thenReturn(answer);
	Mockito.when(m_mockSvc.load(Matchers.any(ProjectActivityFormData.class))).thenReturn(answer);
	Mockito.when(m_mockSvc.store(Matchers.any(ProjectActivityFormData.class))).thenReturn(answer);
    }

    // TODO [Marco Dörfliger] add test cases
}
