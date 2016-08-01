package leslie.org.leslie.client.outline;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;

import leslie.org.leslie.client.permission.PermissionTablePage;
import leslie.org.leslie.client.role.RoleTablePage;
import leslie.org.leslie.client.user.UserTablePage;
import leslie.org.leslie.shared.Icons;
import leslie.org.leslie.shared.user.IUserService.UserPresentationType;

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
}
