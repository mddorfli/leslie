package org.leslie.shared.role;

import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.leslie.client.permission.PermissionTablePageData;
import org.leslie.client.role.RoleFormData;
import org.leslie.client.role.RolePageData;

@TunnelToServer
public interface IRoleService extends IService {

    PermissionTablePageData getPermissionTableData(Long roleNr);

    RolePageData getRoleTableData();

    RoleFormData create(RoleFormData formData) throws ProcessingException;

    RoleFormData store(RoleFormData formData) throws ProcessingException;

    void delete(Long selectedValue) throws ProcessingException;

    Map<Long, String> getRoleMenuItems() throws ProcessingException;

    void assignPermissions(Long roleNr, List<String> permissions, Integer level) throws ProcessingException;

    void revokePermissions(Long roleNr, List<String> permissions) throws ProcessingException;
}
