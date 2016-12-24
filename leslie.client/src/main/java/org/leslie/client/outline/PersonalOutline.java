package org.leslie.client.outline;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.leslie.shared.Icons;

/**
 * <h3>{@link PersonalOutline}</h3>
 *
 * @author Marco Dörfliger
 */
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

}
