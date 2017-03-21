package org.leslie.client.project;

import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.leslie.client.project.ProjectResourcePlanSearchForm.MainBox.GroupBox;
import org.leslie.client.project.ProjectResourcePlanSearchForm.MainBox.GroupBox.DateRangeBox;
import org.leslie.client.project.ProjectResourcePlanSearchForm.MainBox.GroupBox.DateRangeBox.FromField;
import org.leslie.client.project.ProjectResourcePlanSearchForm.MainBox.GroupBox.DateRangeBox.ToField;
import org.leslie.client.project.ProjectResourcePlanSearchForm.MainBox.ResetButton;
import org.leslie.client.project.ProjectResourcePlanSearchForm.MainBox.SearchButton;
import org.leslie.shared.project.ProjectResourcePlanSearchFormData;

@FormData(value = ProjectResourcePlanSearchFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class ProjectResourcePlanSearchForm extends AbstractSearchForm {

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Search");
    }

    public DateRangeBox getDateRangeBox() {
	return getFieldByClass(DateRangeBox.class);
    }

    public FromField getFromField() {
	return getFieldByClass(FromField.class);
    }

    public ToField getToField() {
	return getFieldByClass(ToField.class);
    }

    public GroupBox getGroupBox() {
	return getFieldByClass(GroupBox.class);
    }

    public ResetButton getResetButton() {
	return getFieldByClass(ResetButton.class);
    }

    public MainBox getMainBox() {
	return getFieldByClass(MainBox.class);
    }

    public SearchButton getSearchButton() {
	return getFieldByClass(SearchButton.class);
    }

    public void startSearch() {
	startInternal(new SearchHandler());
    }

    @Order(1000)
    public class MainBox extends AbstractGroupBox {

	@Order(1000)
	public class GroupBox extends AbstractGroupBox {

	    @Order(1000)
	    public class DateRangeBox extends AbstractSequenceBox {
		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("Date");
		}

		@Override
		protected boolean getConfiguredAutoCheckFromTo() {
		    return true;
		}

		@Order(1000)
		public class FromField extends AbstractDateField {
		    @Override
		    protected String getConfiguredLabel() {
			return TEXTS.get("From");
		    }
		}

		@Order(2000)
		public class ToField extends AbstractDateField {
		    @Override
		    protected String getConfiguredLabel() {
			return TEXTS.get("To");
		    }
		}

	    }
	}

	@Order(100000)
	public class SearchButton extends AbstractSearchButton {
	}

	@Order(101000)
	public class ResetButton extends AbstractResetButton {
	}
    }

    public class SearchHandler extends AbstractFormHandler {
	@Override
	protected void execLoad() {
	    getFromField().setValue(DateUtility.truncDate(new Date()));
	}

	@Override
	protected void execStore() {
	}
    }

}
