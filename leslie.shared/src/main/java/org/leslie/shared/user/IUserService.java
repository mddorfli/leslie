package org.leslie.shared.user;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.leslie.client.user.UserFormData;
import org.leslie.client.user.UserPageData;

@TunnelToServer
public interface IUserService extends IService {

    enum UserPresentation {
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

    UserPageData getAdministrationUserTableData();

    UserFormData create(UserFormData formData) throws ProcessingException;

    UserFormData load(UserFormData formData) throws ProcessingException;

    UserFormData store(UserFormData formData) throws ProcessingException;

    void delete(Long selectedValue) throws ProcessingException;

    UserPageData getProjectUserTableData(Long projectId);

    Long getUserId(String username);

    Long getCurrentUserNr();
}
