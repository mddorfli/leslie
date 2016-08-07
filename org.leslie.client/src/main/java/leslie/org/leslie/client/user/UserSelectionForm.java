package leslie.org.leslie.client.user;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import leslie.org.leslie.client.user.UserSelectionForm.MainBox.CancelButton;
import leslie.org.leslie.client.user.UserSelectionForm.MainBox.GroupBox;
import leslie.org.leslie.client.user.UserSelectionForm.MainBox.GroupBox.UserField;
import leslie.org.leslie.client.user.UserSelectionForm.MainBox.OkButton;
import leslie.org.leslie.shared.project.IProjectService;
import leslie.org.leslie.shared.user.UserLookupCall;
import leslie.org.leslie.shared.user.UserSelectionFormData;

@FormData(value = UserSelectionFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class UserSelectionForm extends AbstractForm {

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
		return TEXTS.get("UserSelection");
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
					lookup.setDisabledProjectId(getProjectId());
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
			UserSelectionFormData formData = new UserSelectionFormData();
			exportFormData(formData);
			BEANS.get(IProjectService.class).assignUser(formData);
		}
	}
}
