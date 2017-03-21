package org.leslie.client.vacation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.client.vacation.VacationForm.MainBox.CancelButton;
import org.leslie.client.vacation.VacationForm.MainBox.GroupBox;
import org.leslie.client.vacation.VacationForm.MainBox.GroupBox.ApprovedByField;
import org.leslie.client.vacation.VacationForm.MainBox.GroupBox.DateSequenceBox;
import org.leslie.client.vacation.VacationForm.MainBox.GroupBox.DateSequenceBox.FromField;
import org.leslie.client.vacation.VacationForm.MainBox.GroupBox.DateSequenceBox.ToField;
import org.leslie.client.vacation.VacationForm.MainBox.GroupBox.DescriptionField;
import org.leslie.client.vacation.VacationForm.MainBox.GroupBox.RequestedByField;
import org.leslie.client.vacation.VacationForm.MainBox.OkButton;
import org.leslie.shared.security.permission.ApproveVacationPermission;
import org.leslie.shared.security.permission.RequestVacationPermission;
import org.leslie.shared.user.IUserService;
import org.leslie.shared.user.UserLookupCall;
import org.leslie.shared.vacation.IVacationService;
import org.leslie.shared.vacation.VacationFormData;

@FormData(value = VacationFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class VacationForm extends AbstractForm {

    private Long activityId;

    @FormData
    public Long getActivityId() {
	return activityId;
    }

    @FormData
    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Vacation");
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

    public RequestedByField getRequestedByField() {
	return getFieldByClass(RequestedByField.class);
    }

    public FromField getFromField() {
	return getFieldByClass(FromField.class);
    }

    public ToField getToField() {
	return getFieldByClass(ToField.class);
    }

    public ApprovedByField getApprovedByField() {
	return getFieldByClass(ApprovedByField.class);
    }

    public DateSequenceBox getDateSequenceBox() {
	return getFieldByClass(DateSequenceBox.class);
    }

    public DescriptionField getDescriptionField() {
	return getFieldByClass(DescriptionField.class);
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
	    public class RequestedByField extends AbstractSmartField<Long> {
		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("RequestedBy");
		}

		@Override
		protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
		    return UserLookupCall.class;
		}

		@Override
		protected boolean getConfiguredEnabled() {
		    return false;
		}
	    }

	    @Order(2000)
	    public class DateSequenceBox extends AbstractSequenceBox {

		@Order(2000)
		public class FromField extends AbstractDateField {
		    @Override
		    protected String getConfiguredLabel() {
			return TEXTS.get("From");
		    }

		    @Override
		    protected boolean getConfiguredMandatory() {
			return true;
		    }

		    @Override
		    protected Date execValidateValue(Date rawValue) {
			if (rawValue == null) {
			    return null;
			}

			LocalDateTime fromDate = LocalDateTime.ofInstant(rawValue.toInstant(), ZoneId.systemDefault());
			LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

			if (fromDate.isBefore(today) && !ACCESS.check(new ApproveVacationPermission())) {
			    throw new VetoException(TEXTS.get("DateMustBeOnOrAfterToday"));
			}
			return rawValue;
		    }
		}

		@Order(3000)
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
	    public class DescriptionField extends AbstractStringField {
		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("Description");
		}

		@Override
		protected int getConfiguredGridH() {
		    return 2;
		}
	    }

	    @Order(5000)
	    public class ApprovedByField extends AbstractSmartField<Long> {
		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("ApprovedBy");
		}

		@Override
		protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
		    return UserLookupCall.class;
		}

		@Override
		protected boolean getConfiguredEnabled() {
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
	    IVacationService service = BEANS.get(IVacationService.class);
	    VacationFormData formData = new VacationFormData();
	    exportFormData(formData);
	    formData = service.load(formData);
	    importFormData(formData);

	    getApprovedByField().setVisible(getApprovedByField().getValue() != null);
	    Long userNr = BEANS.get(IUserService.class).getCurrentUserNr();
	    setEnabledGranted((userNr.equals(getRequestedByField().getValue())
		    && getApprovedByField().getValue() == null)
		    || ACCESS.check(new ApproveVacationPermission()));
	}

	@Override
	protected void execStore() {
	    IVacationService service = BEANS.get(IVacationService.class);
	    VacationFormData formData = new VacationFormData();
	    exportFormData(formData);
	    service.store(formData);
	}
    }

    public class NewHandler extends AbstractFormHandler {

	@Override
	protected void execLoad() {
	    IVacationService service = BEANS.get(IVacationService.class);
	    VacationFormData formData = new VacationFormData();
	    exportFormData(formData);
	    formData = service.prepareCreate(formData);
	    importFormData(formData);

	    setEnabledPermission(new RequestVacationPermission());
	    getApprovedByField().setVisibleGranted(false);
	}

	@Override
	protected void execStore() {
	    IVacationService service = BEANS.get(IVacationService.class);
	    VacationFormData formData = new VacationFormData();
	    exportFormData(formData);
	    service.create(formData);
	}
    }
}
