package org.leslie.client;

import java.util.List;

import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.client.ui.desktop.bookmark.menu.AbstractBookmarkMenu;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutlineViewButton;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.client.ui.form.ScoutInfoForm;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.leslie.client.outline.AdministrationOutline;
import org.leslie.client.outline.PersonalOutline;
import org.leslie.client.outline.ProjectOutline;
import org.leslie.shared.Icons;

/**
 * <h3>{@link Desktop}</h3>
 *
 * @author Marco Dörfliger
 */
public class Desktop extends AbstractDesktop {
    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("ApplicationTitle");
    }

    @Override
    protected String getConfiguredLogoId() {
	return Icons.AppLogo;
    }

    @Override
    protected List<Class<? extends IOutline>> getConfiguredOutlines() {
	return CollectionUtility.<Class<? extends IOutline>>arrayList(
		PersonalOutline.class, ProjectOutline.class, AdministrationOutline.class);
    }

    @Override
    protected void execGuiAttached() {
	super.execGuiAttached();
	selectFirstVisibleOutline();
    }

    protected void selectFirstVisibleOutline() {
	for (IOutline outline : getAvailableOutlines()) {
	    if (outline.isEnabled() && outline.isVisible()) {
		setOutline(outline);
		break;
	    }
	}
    }

    @Order(1000)
    public class FileMenu extends AbstractMenu {

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("File");
	}

	@Order(1000)
	public class ExitMenu extends AbstractMenu {

	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("Exit");
	    }

	    @Override
	    protected void execAction() {
		ClientSessionProvider.currentSession(ClientSession.class).stop();
	    }
	}
    }

    @Order(2000)
    public class BookmarkMenu extends AbstractBookmarkMenu {
	public BookmarkMenu() {
	    super(Desktop.this);
	}
    }

    @Order(3000)
    public class HelpMenu extends AbstractMenu {

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("Help");
	}

	@Order(1000)
	public class AboutMenu extends AbstractMenu {

	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("About");
	    }

	    @Override
	    protected void execAction() {
		ScoutInfoForm form = new ScoutInfoForm();
		form.startModify();
	    }
	}
    }

    @Order(1000)
    public class PersonalOutlineViewButton extends AbstractOutlineViewButton {

	public PersonalOutlineViewButton() {
	    this(PersonalOutline.class);
	}

	protected PersonalOutlineViewButton(Class<? extends PersonalOutline> outlineClass) {
	    super(Desktop.this, outlineClass);
	}

	@Override
	protected String getConfiguredKeyStroke() {
	    return IKeyStroke.F2;
	}
    }

    @Order(2000)
    public class ProjectOutlineViewButton extends AbstractOutlineViewButton {

	public ProjectOutlineViewButton() {
	    this(ProjectOutline.class);
	}

	protected ProjectOutlineViewButton(Class<? extends ProjectOutline> outlineClass) {
	    super(Desktop.this, outlineClass);
	}

	@Override
	protected DisplayStyle getConfiguredDisplayStyle() {
	    return DisplayStyle.TAB;
	}

	@Override
	protected String getConfiguredKeyStroke() {
	    return IKeyStroke.F3;
	}
    }

    @Order(3000)
    public class AdministrationOutlineViewButton extends AbstractOutlineViewButton {

	public AdministrationOutlineViewButton() {
	    this(AdministrationOutline.class);
	}

	protected AdministrationOutlineViewButton(Class<? extends AdministrationOutline> outlineClass) {
	    super(Desktop.this, outlineClass);
	}

	@Override
	protected DisplayStyle getConfiguredDisplayStyle() {
	    return DisplayStyle.TAB;
	}

	@Override
	protected String getConfiguredKeyStroke() {
	    return IKeyStroke.F10;
	}
    }
}
