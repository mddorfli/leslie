package org.leslie.client.outline;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.permission.PermissionTablePage;
import org.leslie.client.role.RoleTablePage;
import org.leslie.client.user.UserTablePage;
import org.leslie.shared.Icons;
import org.leslie.shared.security.permission.ReadAdministrationPermission;
import org.leslie.shared.user.IUserService.UserPresentationType;

public class AdministrationOutline extends AbstractOutline {

    @Override
    protected void createChildPagesInternal(List<IPage<?>> pageList) {
	pageList.add(new RoleTablePage());
	pageList.add(new UserTablePage(UserPresentationType.ADMINISTRATION));
	pageList.add(new PermissionTablePage());
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Administration");
    }

    @Override
    protected String getConfiguredIconId() {
	return Icons.Gear;
    }

    @Override
    protected boolean getConfiguredVisible() {
	return ACCESS.check(new ReadAdministrationPermission());
    }
}
