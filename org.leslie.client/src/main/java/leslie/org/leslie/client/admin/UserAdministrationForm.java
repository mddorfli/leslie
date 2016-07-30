package leslie.org.leslie.client.admin;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import leslie.org.leslie.client.admin.RoleForm.MainBox.GroupBox;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.CancelButton;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.EmailField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.FirstNameField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.LastNameField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.LockedField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.OrganisationField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.PasswordField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.PrivateField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.RolesField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.GroupBox.UsernameField;
import leslie.org.leslie.client.admin.UserAdministrationForm.MainBox.OkButton;
import leslie.org.leslie.shared.admin.IUserAdministrationService;
import leslie.org.leslie.shared.admin.UserAdministrationFormData;
import leslie.org.leslie.shared.admin.lookup.RoleLookupCall;
import leslie.org.leslie.shared.security.UpdateAdministrationPermission;

@FormData(value = UserAdministrationFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class UserAdministrationForm extends AbstractForm {

	private Long userNr;

	public UserAdministrationForm() throws ProcessingException {
		super();
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("User");
	}

	@FormData
	public Long getUserNr() {
		return userNr;
	}

	@FormData
	public void setUserNr(Long userNr) {
		this.userNr = userNr;
	}

	public void startModify() throws ProcessingException {
		startInternal(new ModifyHandler());
	}

	public void startNew() throws ProcessingException {
		startInternal(new NewHandler());
	}

	public CancelButton getCancelButton() {
		return getFieldByClass(CancelButton.class);
	}

	public EmailField getEmailField() {
		return getFieldByClass(EmailField.class);
	}

	public FirstNameField getFirstNameField() {
		return getFieldByClass(FirstNameField.class);
	}

	public GroupBox getGroupBox() {
		return getFieldByClass(GroupBox.class);
	}

	public LastNameField getLastNameField() {
		return getFieldByClass(LastNameField.class);
	}

	public LockedField getLockedField() {
		return getFieldByClass(LockedField.class);
	}

	public MainBox getMainBox() {
		return getFieldByClass(MainBox.class);
	}

	public OkButton getOkButton() {
		return getFieldByClass(OkButton.class);
	}

	public OrganisationField getOrganisationField() {
		return getFieldByClass(OrganisationField.class);
	}

	public PasswordField getPasswordField() {
		return getFieldByClass(PasswordField.class);
	}

	/**
	 * @return the PrivateField
	 */
	public PrivateField getPrivateField() {
		return getFieldByClass(PrivateField.class);
	}

	public RolesField getRolesField() {
		return getFieldByClass(RolesField.class);
	}

	public UsernameField getUsernameField() {
		return getFieldByClass(UsernameField.class);
	}

	@Order(10.0)
	public class MainBox extends AbstractGroupBox {

		@Override
		protected int getConfiguredGridColumnCount() {
			return 2;
		}

		@Order(10.0)
		public class GroupBox extends AbstractGroupBox {

			@Order(10.0)
			public class UsernameField extends AbstractStringField {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Username");
				}

				@Override
				protected boolean getConfiguredMandatory() {
					return true;
				}

				@Override
				protected int getConfiguredMaxLength() {
					return 10;
				}
			}

			@Order(20.0)
			public class FirstNameField extends AbstractStringField {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("FirstName");
				}
			}

			@Order(30.0)
			public class LastNameField extends AbstractStringField {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("LastName");
				}
			}

			@Order(40.0)
			public class OrganisationField extends AbstractStringField {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Organisation");
				}
			}

			@Order(50.0)
			public class EmailField extends AbstractStringField {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Email");
				}

				@Override
				protected String execValidateValue(String rawValue) throws ProcessingException {
					if (!StringUtility.isNullOrEmpty(rawValue) && !rawValue.matches("[^ @]+@[^ @]+")) {
						throw new VetoException("");
					}
					return rawValue;
				}
			}

			@Order(90.0)
			public class PasswordField extends AbstractStringField {

				@Override
				protected int getConfiguredGridX() {
					return 2;
				}

				@Override
				protected boolean getConfiguredInputMasked() {
					return true;
				}

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("NewPassword");
				}

				@Override
				protected int getConfiguredMaxLength() {
					return 32;
				}
			}

			@Order(100.0)
			public class LockedField extends AbstractBooleanField {

				@Override
				protected int getConfiguredGridX() {
					return 2;
				}

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Locked");
				}
			}

			@Order(110.0)
			public class RolesField extends AbstractListBox<Long> {

				@Override
				protected int getConfiguredGridH() {
					return 5;
				}

				@Override
				protected int getConfiguredGridX() {
					return 2;
				}

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Roles");
				}

				@Override
				protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
					return RoleLookupCall.class;
				}
			}

			@Order(120.0)
			public class PrivateField extends AbstractBooleanField {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Private");
				}
			}
		}

		@Order(30.0)
		public class OkButton extends AbstractOkButton {
		}

		@Order(40.0)
		public class CancelButton extends AbstractCancelButton {
		}
	}

	public class ModifyHandler extends AbstractFormHandler {

		@Override
		public void execLoad() throws ProcessingException {
			IUserAdministrationService service = BEANS.get(IUserAdministrationService.class);
			UserAdministrationFormData formData = new UserAdministrationFormData();
			exportFormData(formData);
			formData = service.load(formData);
			importFormData(formData);
			setEnabledPermission(new UpdateAdministrationPermission());
		}

		@Override
		public void execStore() throws ProcessingException {
			IUserAdministrationService service = BEANS.get(IUserAdministrationService.class);
			UserAdministrationFormData formData = new UserAdministrationFormData();
			exportFormData(formData);
			formData = service.store(formData);
		}
	}

	public class NewHandler extends AbstractFormHandler {

		@Override
		public void execLoad() throws ProcessingException {
			UserAdministrationFormData formData = new UserAdministrationFormData();
			importFormData(formData);
			getPasswordField().setMandatory(true);
		}

		@Override
		public void execStore() throws ProcessingException {
			IUserAdministrationService service = BEANS.get(IUserAdministrationService.class);
			UserAdministrationFormData formData = new UserAdministrationFormData();
			exportFormData(formData);
			formData = service.create(formData);
		}
	}
}
