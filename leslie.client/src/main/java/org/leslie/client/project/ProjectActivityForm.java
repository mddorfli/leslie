package org.leslie.client.project;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.client.project.ProjectActivityForm.MainBox.CancelButton;
import org.leslie.client.project.ProjectActivityForm.MainBox.GroupBox;
import org.leslie.client.project.ProjectActivityForm.MainBox.GroupBox.DurationBox;
import org.leslie.client.project.ProjectActivityForm.MainBox.GroupBox.DurationBox.FromField;
import org.leslie.client.project.ProjectActivityForm.MainBox.GroupBox.DurationBox.ToField;
import org.leslie.client.project.ProjectActivityForm.MainBox.GroupBox.PercentageField;
import org.leslie.client.project.ProjectActivityForm.MainBox.GroupBox.UserField;
import org.leslie.client.project.ProjectActivityForm.MainBox.OkButton;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.user.UserLookupCall;

@FormData(value = ProjectActivityFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class ProjectActivityForm extends AbstractForm {

    private Long activityId;

    @FormData
    public Long getActivityId() {
	return activityId;
    }

    @FormData
    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

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
	return TEXTS.get("ProjectResource");
    }

    public void startModify() {
	startInternalExclusive(new ModifyHandler());
    }

    public void startNew() {
	startInternal(new NewHandler());
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

    public UserField getUserField() {
	return getFieldByClass(UserField.class);
    }

    public DurationBox getDurationBox() {
	return getFieldByClass(DurationBox.class);
    }

    public PercentageField getPercentageField() {
	return getFieldByClass(PercentageField.class);
    }

    public FromField getFromField() {
	return getFieldByClass(FromField.class);
    }

    public ToField getToField() {
	return getFieldByClass(ToField.class);
    }

    public OkButton getOkButton() {
	return getFieldByClass(OkButton.class);
    }

    @Order(1000)
    public class MainBox extends AbstractGroupBox {

	@Override
	protected int getConfiguredGridColumnCount() {
	    return 1;
	}

	@Order(1000)
	public class GroupBox extends AbstractGroupBox {

	    @Order(1000)
	    public class UserField extends AbstractSmartField<Long> {
		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("User");
		}

		@Override
		protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
		    return UserLookupCall.class;
		}

		@Override
		protected void execPrepareLookup(ILookupCall<Long> call) {
		    UserLookupCall lookup = (UserLookupCall) call;
		    lookup.setProjectId(getProjectId());
		}

		@Override
		protected boolean getConfiguredMandatory() {
		    return true;
		}
	    }

	    @Order(2000)
	    public class DurationBox extends AbstractSequenceBox {

		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("Duration");
		}

		@Override
		protected boolean getConfiguredAutoCheckFromTo() {
		    return true;
		}

		@Override
		protected boolean getConfiguredMandatory() {
		    return true;
		}

		@Order(1000)
		public class FromField extends AbstractDateField {

		    @Override
		    protected String getConfiguredLabel() {
			return TEXTS.get("From");
		    }

		    @Override
		    protected boolean getConfiguredMandatory() {
			return true;
		    }
		}

		@Order(2000)
		public class ToField extends AbstractDateField {

		    @Override
		    protected String getConfiguredLabel() {
			return TEXTS.get("To");
		    }

		    @Override
		    protected boolean getConfiguredMandatory() {
			return true;
		    }
		}

	    }

	    @Order(3000)
	    public class PercentageField extends AbstractBigDecimalField {
		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("Percentage");
		}

		@Override
		protected BigDecimal getConfiguredMinValue() {
		    return new BigDecimal("0");
		}

		@Override
		protected BigDecimal getConfiguredMaxValue() {
		    return new BigDecimal("100");
		}

		@Override
		protected boolean getConfiguredMandatory() {
		    return true;
		}

		@Override
		protected int getConfiguredMaxFractionDigits() {
		    return 0;
		}

		@Override
		protected int getConfiguredMultiplier() {
		    return 100;
		}

		@Override
		protected boolean getConfiguredPercent() {
		    return true;
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
	    IProjectActivityService service = BEANS.get(IProjectActivityService.class);
	    ProjectActivityFormData formData = new ProjectActivityFormData();
	    exportFormData(formData);
	    formData = service.load(formData);
	    importFormData(formData);

	    setEnabledPermission(new ManageProjectPermission(getProjectId()));
	}

	@Override
	protected void execStore() {
	    IProjectActivityService service = BEANS.get(IProjectActivityService.class);
	    ProjectActivityFormData formData = new ProjectActivityFormData();
	    exportFormData(formData);
	    service.store(formData);
	}
    }

    public class NewHandler extends AbstractFormHandler {

	@Override
	protected void execLoad() {
	    IProjectActivityService service = BEANS.get(IProjectActivityService.class);
	    ProjectActivityFormData formData = new ProjectActivityFormData();
	    exportFormData(formData);
	    formData = service.prepareCreate(formData);
	    importFormData(formData);
	}

	@Override
	protected void execStore() {
	    IProjectActivityService service = BEANS.get(IProjectActivityService.class);
	    ProjectActivityFormData formData = new ProjectActivityFormData();
	    exportFormData(formData);
	    service.create(formData);
	}
    }
}
