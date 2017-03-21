package org.leslie.client.activity;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.shared.TEXTS;

public class PersonalCalendarNodePage extends AbstractPageWithNodes {

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Calendar");
    }

    @Override
    protected Class<? extends IForm> getConfiguredDetailForm() {
	return PersonalCalendarForm.class;
    }

    @Override
    protected boolean getConfiguredLeaf() {
	return true;
    }
}
