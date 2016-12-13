package leslie.org.leslie.client.vacation;

import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.AbstractIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import leslie.org.leslie.client.vacation.VacationForm.MainBox.CancelButton;
import leslie.org.leslie.client.vacation.VacationForm.MainBox.GroupBox;
import leslie.org.leslie.client.vacation.VacationForm.MainBox.OkButton;
import leslie.org.leslie.client.vacation.VacationForm.MainBox.GroupBox.ApprovedByField;
import leslie.org.leslie.client.vacation.VacationForm.MainBox.GroupBox.ApprovedField;
import leslie.org.leslie.client.vacation.VacationForm.MainBox.GroupBox.DaysField;
import leslie.org.leslie.client.vacation.VacationForm.MainBox.GroupBox.FromField;
import leslie.org.leslie.client.vacation.VacationForm.MainBox.GroupBox.ToField;
import leslie.org.leslie.shared.appointment.IVacationAppointmentService;
import leslie.org.leslie.shared.security.permission.CreateVacationPermission;
import leslie.org.leslie.shared.security.permission.UpdateVacationPermission;
import leslie.org.leslie.shared.user.UserLookupCall;
import leslie.org.leslie.shared.work.VacationAppointmentFormData;

@FormData(value = VacationAppointmentFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class VacationForm extends AbstractForm {

	private long id;

	@FormData
	public long getId() {
		return id;
	}

	@FormData
	public void setId(long id) {
		this.id = id;
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
	public FromField getFromField() {
		return getFieldByClass(FromField.class);
	}
	public ToField getToField() {
		return getFieldByClass(ToField.class);
	}
	public DaysField getDaysField() {
		return getFieldByClass(DaysField.class);
	}
	public ApprovedField getApprovedField() {
		return getFieldByClass(ApprovedField.class);
	}
	public ApprovedByField getApprovedByField() {
		return getFieldByClass(ApprovedByField.class);
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
			public class FromField extends AbstractDateField {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("From");
				}

				@Override
				protected void execChangedValue() {
					getDaysField().recalculateDays();
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

				@Override
				protected Date execValidateValue(Date newToDate) {
					Date fromDate = getFromField().getValue();
					if (newToDate != null && fromDate != null && newToDate.before(fromDate)) {
						throw new VetoException(TEXTS.get("ValueMustBeOnOrAfter0", getFromField().getDisplayText()));
					}
					return newToDate;
				}

				@Override
				protected void execChangedValue() {
					getDaysField().recalculateDays();
				}

			}

			@Order(3000)
			public class DaysField extends AbstractIntegerField {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Days");
				}

				@Override
				protected boolean getConfiguredEnabled() {
					return false;
				}

				private void recalculateDays() {
					Date from = getFromField().getValue();
					Date to = getToField().getValue();
					if (from == null || to == null) {
						setValue(null);
					} else {
						setValue(DateUtility.getDaysBetween(from, to) + 1);
					}
				}
			}

			@Order(4000)
			public class ApprovedField extends AbstractBooleanField {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Approved");
				}

				@Override
				protected boolean getConfiguredEnabled() {
					return false;
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
			IVacationAppointmentService service = BEANS.get(IVacationAppointmentService.class);
			VacationAppointmentFormData formData = new VacationAppointmentFormData();
			exportFormData(formData);
			formData = service.load(formData);
			importFormData(formData);

			getDaysField().recalculateDays();
			getApprovedField().setValue(getApprovedByField().getValue() != null);

			setEnabledPermission(new UpdateVacationPermission());
		}
		@Override
		protected void execStore() {
			IVacationAppointmentService service = BEANS.get(IVacationAppointmentService.class);
			VacationAppointmentFormData formData = new VacationAppointmentFormData();
			exportFormData(formData);
			service.store(formData);
		}
	}
	public class NewHandler extends AbstractFormHandler {

		@Override
		protected void execLoad() {
			IVacationAppointmentService service = BEANS.get(IVacationAppointmentService.class);
			VacationAppointmentFormData formData = new VacationAppointmentFormData();
			exportFormData(formData);
			formData = service.prepareCreate(formData);
			importFormData(formData);

			setEnabledPermission(new CreateVacationPermission());
		}
		@Override
		protected void execStore() {
			IVacationAppointmentService service = BEANS.get(IVacationAppointmentService.class);
			VacationAppointmentFormData formData = new VacationAppointmentFormData();
			exportFormData(formData);
			service.create(formData);
		}
	}
}
