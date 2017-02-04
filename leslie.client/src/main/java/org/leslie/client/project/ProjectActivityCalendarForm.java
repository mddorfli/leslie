package org.leslie.client.project;

import java.util.Date;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.basic.calendar.AbstractCalendar;
import org.eclipse.scout.rt.client.ui.basic.calendar.provider.AbstractCalendarItemProvider;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.calendarfield.AbstractCalendarField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.calendar.ICalendarItem;
import org.leslie.client.project.ProjectActivityCalendarForm.MainBox.CancelButton;
import org.leslie.client.project.ProjectActivityCalendarForm.MainBox.GroupBox;
import org.leslie.client.project.ProjectActivityCalendarForm.MainBox.GroupBox.CalendarField;
import org.leslie.client.project.ProjectActivityCalendarForm.MainBox.OkButton;
import org.leslie.shared.project.IProjectService;
import org.leslie.shared.project.ProjectActivityCalendarFormData;

@FormData(value = ProjectActivityCalendarFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class ProjectActivityCalendarForm extends AbstractForm {

    private Long projectId;

    @FormData
    public Long getProjectId() {
	return projectId;
    }

    @FormData
    public void setProjectId(Long projectId) {
	this.projectId = projectId;
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Calendar");
    }

    public void startModify() {
	startInternalExclusive(new ModifyHandler());
    }

    public void startNew() {
	startInternal(new NewHandler());
    }

    public void startView() {
	startInternalExclusive(new ViewHandler());
    }

    public CancelButton getCancelButton() {
	return getFieldByClass(CancelButton.class);
    }

    public MainBox getMainBox() {
	return getFieldByClass(MainBox.class);
    }

    public GroupBox getGroupBox() {
	return getFieldByClass(GroupBox.class);
    }

    public CalendarField getCalendarField() {
	return getFieldByClass(CalendarField.class);
    }

    public OkButton getOkButton() {
	return getFieldByClass(OkButton.class);
    }

    @Override
    protected int getConfiguredDisplayHint() {
	return DISPLAY_HINT_VIEW;
    }

    @Override
    protected String getConfiguredDisplayViewId() {
	return VIEW_ID_S;
    }

    @Override
    public Object computeExclusiveKey() {
	return projectId;
    }

    @Order(1000)
    public class MainBox extends AbstractGroupBox {

	@Order(1000)
	public class GroupBox extends AbstractGroupBox {

	    @Order(1000)
	    public class CalendarField extends AbstractCalendarField<CalendarField.Calendar> {

		public class Calendar extends AbstractCalendar {

		    @Order(1000)
		    public class MyCalendarItemProvider extends AbstractCalendarItemProvider {

			@Override
			protected void execLoadItems(Date minDate, Date maxDate, Set<ICalendarItem> result) {
			    // BEANS.get(IProjectService.class).getProjectActivityData(getProjectId(),
			    // minDate, maxDate);
			}
		    }
		}

		@Override
		protected int getConfiguredGridH() {
		    return 10;
		}

		@Override
		protected boolean getConfiguredLabelVisible() {
		    return false;
		}
	    }

	}

	@Order(100000)
	public class OkButton extends AbstractOkButton {
	}

	@Order(101000)
	public class CancelButton extends AbstractCancelButton {
	}
    }

    public class ModifyHandler extends AbstractFormHandler {

	@Override
	protected void execLoad() {
	}

	@Override
	protected void execStore() {
	}
    }

    public class NewHandler extends AbstractFormHandler {

	@Override
	protected void execLoad() {
	}

	@Override
	protected void execStore() {
	}
    }

    public class ViewHandler extends AbstractFormHandler {
	@Override
	protected void execLoad() {
	    getOkButton().setVisibleGranted(false);
	    getCancelButton().setVisibleGranted(false);
	}

	@Override
	protected boolean getConfiguredOpenExclusive() {
	    return true;
	}

	@Override
	protected void execStore() {
	}
    }

}
