package org.leslie.client.project;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.shared.TEXTS;

public class ProjectActivityCalendarNodePage extends AbstractPageWithNodes {

    private Long projectId;

    public Long getProjectId() {
	return projectId;
    }

    public void setProjectId(Long projectId) {
	this.projectId = projectId;
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Calendar");
    }

    @Override
    protected boolean getConfiguredLazyExpandingEnabled() {
	return true;
    }

    @Override
    protected Class<? extends IForm> getConfiguredDetailForm() {
	return ProjectActivityCalendarForm.class;
    }

    @Override
    protected void execInitDetailForm() {
	ProjectActivityCalendarForm form = (ProjectActivityCalendarForm) getDetailForm();
	form.setProjectId(projectId);
	form.startView();
    }
}
