package org.leslie.client.activity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.basic.calendar.AbstractCalendar;
import org.eclipse.scout.rt.client.ui.basic.calendar.provider.AbstractCalendarItemProvider;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.fields.calendarfield.AbstractCalendarField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.calendar.ICalendarItem;
import org.leslie.client.activity.PersonalCalendarForm.MainBox.CalendarField;
import org.leslie.shared.project.IProjectActivityService;
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
