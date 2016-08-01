package leslie.org.leslie.shared.appointment;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import leslie.org.leslie.shared.work.VacationAppointmentFormData;
import leslie.org.leslie.shared.work.VacationAppointmentTablePageData;

@TunnelToServer
public interface IVacationAppointmentService extends IService {

	VacationAppointmentTablePageData getVacationAppointmentTableData(SearchFilter filter);

	VacationAppointmentFormData prepareCreate(VacationAppointmentFormData formData);

	VacationAppointmentFormData create(VacationAppointmentFormData formData);

	VacationAppointmentFormData load(VacationAppointmentFormData formData);

	VacationAppointmentFormData store(VacationAppointmentFormData formData);

	void delete(Long selectedValue);
}
