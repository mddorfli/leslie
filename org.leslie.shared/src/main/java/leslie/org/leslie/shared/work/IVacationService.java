package leslie.org.leslie.shared.work;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

@TunnelToServer
public interface IVacationService extends IService {

	VacationsTablePageData getVacationsTableData(SearchFilter filter);

	VacationFormData prepareCreate(VacationFormData formData);

	VacationFormData create(VacationFormData formData);

	VacationFormData load(VacationFormData formData);

	VacationFormData store(VacationFormData formData);

	void delete(Long selectedValue);
}
