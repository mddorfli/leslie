package org.leslie.client.outline;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.project.ProjectTablePage;
import org.leslie.client.user.UserTablePage;
import org.leslie.shared.Icons;
import org.leslie.shared.security.permission.AssessSkillPermission;
import org.leslie.shared.user.IUserService.UserPresentation;

/**
 * <h3>{@link WorkOutline}</h3>
 *
 * @author Marco Dörfliger
 */
@Order(2000)
public class WorkOutline extends AbstractOutline {

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Work");
	}

	@Override
	protected String getConfiguredIconId() {
		return Icons.Pencil;
	}

	@Override
	protected void execCreateChildPages(List<IPage<?>> pageList) {
		pageList.add(new ProjectTablePage());
		if (ACCESS.getLevel(new AssessSkillPermission()) == AssessSkillPermission.LEVEL_ALL) {
			pageList.add(new UserTablePage(UserPresentation.SKILL_ASSESSMENT));
		}
	}
}
