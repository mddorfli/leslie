package leslie.org.leslie.client.work;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.mock.BeanMock;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import leslie.org.leslie.shared.work.IVacationService;
import leslie.org.leslie.shared.work.VacationFormData;

@RunWithSubject("anonymous")
@RunWith(ClientTestRunner.class)
@RunWithClientSession(TestEnvironmentClientSession.class)
public class VacationFormTest {

	@BeanMock
	private IVacationService m_mockSvc;

	@Before
	public void setup() {
		VacationFormData answer = new VacationFormData();
		Mockito.when(m_mockSvc.prepareCreate(Matchers.any(VacationFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.create(Matchers.any(VacationFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.load(Matchers.any(VacationFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.store(Matchers.any(VacationFormData.class))).thenReturn(answer);
	}

	// TODO [kiwi] add test cases
}
