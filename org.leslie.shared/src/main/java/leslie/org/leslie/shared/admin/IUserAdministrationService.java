package leslie.org.leslie.shared.admin;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IUserAdministrationService extends IService {

	UserAdministrationFormData create(UserAdministrationFormData formData) throws ProcessingException;

	UserAdministrationFormData load(UserAdministrationFormData formData) throws ProcessingException;

	UserAdministrationFormData store(UserAdministrationFormData formData) throws ProcessingException;

	void delete(Long selectedValue) throws ProcessingException;
}
