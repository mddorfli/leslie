package leslie.org.leslie.shared.admin;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IAdministrationOutlineService extends IService {

	PermissionTablePageData getPermissionTableData(Long roleNr) throws ProcessingException;

	RolePageData getRoleTableData() throws ProcessingException;

	UserPageData getUserTableData() throws ProcessingException;
}
