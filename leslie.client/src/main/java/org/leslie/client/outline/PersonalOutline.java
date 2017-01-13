package org.leslie.client.outline;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.leslie.client.vacation.VacationTablePage;
import org.leslie.shared.Icons;

@Order(1000)
public class PersonalOutline extends AbstractOutline {
    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Personal");
    }

    @Override
    protected String getConfiguredIconId() {
	return Icons.User;
    }

    @Override
    protected void execCreateChildPages(List<IPage<?>> pageList) {
	pageList.add(new VacationTablePage());
    }

}
