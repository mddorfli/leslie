package leslie.org.leslie.client.admin;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;

import leslie.org.leslie.shared.Icons;
import leslie.org.leslie.shared.admin.IUserService.UserPresentationType;

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
