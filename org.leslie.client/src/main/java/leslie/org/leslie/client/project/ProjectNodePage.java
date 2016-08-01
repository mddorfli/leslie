package leslie.org.leslie.client.project;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;

import leslie.org.leslie.client.user.UserTablePage;
import leslie.org.leslie.shared.user.IUserService.UserPresentationType;

public class ProjectNodePage extends AbstractPageWithNodes {

	private long projectId;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Project");
	}

	@Override
	protected void execCreateChildPages(List<IPage<?>> pageList) {
		UserTablePage userPage = new UserTablePage(UserPresentationType.PROJECT);
		userPage.setProjectId(projectId);
		pageList.add(userPage);
	}
}
