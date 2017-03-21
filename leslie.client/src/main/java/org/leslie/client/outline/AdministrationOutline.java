package org.leslie.client.outline;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.permission.PermissionTablePage;
import org.leslie.client.role.RoleTablePage;
import org.leslie.client.skill.SkillCategoryTablePage;
import org.leslie.client.skill.SkillTablePage;
import org.leslie.client.user.UserTablePage;
import org.leslie.shared.Icons;
import org.leslie.shared.security.permission.ReadAdministrationPermission;
import org.leslie.shared.skill.ISkillService.SkillPresentation;
import org.leslie.shared.user.IUserService.UserPresentation;;
public class AdministrationOutline extends AbstractOutline {

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

	@Override
	protected void createChildPagesInternal(List<IPage<?>> pageList) {
		pageList.add(new RoleTablePage());
		pageList.add(new UserTablePage(UserPresentation.ADMINISTRATION));
		pageList.add(new PermissionTablePage());
		pageList.add(new SkillCategoryTablePage());
		pageList.add(new SkillTablePage(SkillPresentation.ADMIN));
	}
}
