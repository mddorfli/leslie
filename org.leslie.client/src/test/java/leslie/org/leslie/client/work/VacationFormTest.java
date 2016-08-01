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

import leslie.org.leslie.shared.appointment.IVacationAppointmentService;
import leslie.org.leslie.shared.work.VacationAppointmentFormData;

@RunWithSubject("anonymous")
@RunWith(ClientTestRunner.class)
@RunWithClientSession(TestEnvironmentClientSession.class)
public class VacationFormTest {

	@BeanMock
	private IVacationAppointmentService m_mockSvc;

	@Before
	public void setup() {
		VacationAppointmentFormData answer = new VacationAppointmentFormData();
		Mockito.when(m_mockSvc.prepareCreate(Matchers.any(VacationAppointmentFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.create(Matchers.any(VacationAppointmentFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.load(Matchers.any(VacationAppointmentFormData.class))).thenReturn(answer);
		Mockito.when(m_mockSvc.store(Matchers.any(VacationAppointmentFormData.class))).thenReturn(answer);
	}

	// TODO [kiwi] add test cases
}
