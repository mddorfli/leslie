package leslie.org.leslie.client.project;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.mock.BeanMock;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import leslie.org.leslie.shared.project.IProjectService;
import leslie.org.leslie.shared.project.ProjectFormData;

@RunWithSubject("anonymous")
@RunWith(ClientTestRunner.class)
@RunWithClientSession(TestEnvironmentClientSession.class)
public class ProjectFormTest {

	@BeanMock
	private IProjectService m_mockSvc;

	@Before
	public void setup() {
		ProjectFormData answer = new ProjectFormData();
		Mockito.when(m_mockSvc.prepareCreate(Matchers.any(ProjectFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.create(Matchers.any(ProjectFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.load(Matchers.any(ProjectFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.store(Matchers.any(ProjectFormData.class))).thenReturn(answer);
	}

	// TODO [kiwi] add test cases
}
