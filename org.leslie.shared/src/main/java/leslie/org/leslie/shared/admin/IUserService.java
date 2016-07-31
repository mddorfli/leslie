package leslie.org.leslie.shared.admin;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IUserService extends IService {

	enum UserPresentationType {
		/**
		 * Shows all users and all columns. <br>
		 * Enables menus for CRUD operations on users.
		 */
		ADMINISTRATION,
		/**
		 * Shows users of a given project. <br>
		 * Only display name column is shown.
		 */
		PROJECT
	}

	UserPageData getUserTableData();

	UserFormData create(UserFormData formData) throws ProcessingException;

	UserFormData load(UserFormData formData) throws ProcessingException;

	UserFormData store(UserFormData formData) throws ProcessingException;

	void delete(Long selectedValue) throws ProcessingException;
}
