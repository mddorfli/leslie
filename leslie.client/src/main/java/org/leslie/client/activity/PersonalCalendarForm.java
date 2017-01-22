package org.leslie.client.activity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.CalendarMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.basic.calendar.AbstractCalendar;
import org.eclipse.scout.rt.client.ui.basic.calendar.provider.AbstractCalendarItemProvider;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.fields.calendarfield.AbstractCalendarField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.calendar.ICalendarItem;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.activity.PersonalCalendarForm.MainBox.CalendarField;
import org.leslie.client.vacation.VacationForm;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.security.permission.RequestVacationPermission;
import org.leslie.shared.vacation.IVacationService;

public class PersonalCalendarForm extends AbstractForm {

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Calendar");
    }

    public MainBox getMainBox() {
	return getFieldByClass(MainBox.class);
    }

    public CalendarField getCalendarField() {
	return getFieldByClass(CalendarField.class);
    }

    @Order(1000)
    public class MainBox extends AbstractGroupBox {

	@Order(1000)
	public class CalendarField extends AbstractCalendarField<CalendarField.Calendar> {
	    public class Calendar extends AbstractCalendar {

		@Order(1000)
		public class VacationCalendarItemProvider extends AbstractCalendarItemProvider {
		    @Override
		    protected void execLoadItems(Date minDate, Date maxDate, Set<ICalendarItem> result) {
			List<ICalendarItem> items = BEANS.get(IVacationService.class).getCalendarItems(
				minDate, maxDate);
			result.addAll(items);
		    }

		    @Order(1000)
		    public class RequestVacationMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
			    return TEXTS.get("RequestVacation_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
			    return CollectionUtility.hashSet(CalendarMenuType.EmptySpace);
			}

			@Override
			protected boolean getConfiguredVisible() {
			    return ACCESS.check(new RequestVacationPermission());
			}

			@Override
			protected void execAction() {
			    VacationForm form = new VacationForm();
			    form.startNew();
			    form.waitFor();
			    if (form.isFormStored()) {
				reloadCalendarItems();
			    }
			}
		    }

		    @Order(2000)
		    public class UpdateVacationMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
			    return TEXTS.get("UpdateVacation_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
			    return CollectionUtility.hashSet(CalendarMenuType.CalendarComponent);
			}

			@Override
			protected void execAction() {
			    ICalendarItem selected = getCalendar().getSelectedComponent().getItem();
			    VacationForm form = new VacationForm();
			    form.setActivityId((Long) selected.getItemId());
			    form.startModify();
			    form.waitFor();
			    if (form.isFormStored()) {
				reloadCalendarItems();
			    }
			}
		    }
		}

		@Order(2000)
		public class ProjectCalendarItemProvider extends AbstractCalendarItemProvider {
		    @Override
		    protected void execLoadItems(Date minDate, Date maxDate, Set<ICalendarItem> result) {
			List<ICalendarItem> items = BEANS.get(IProjectActivityService.class).getCalendarItems(
				minDate, maxDate);
			result.addAll(items);
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
}
