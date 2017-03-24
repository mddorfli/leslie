package org.leslie.client.skill;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.client.skill.SkillForm.MainBox.CancelButton;
import org.leslie.client.skill.SkillForm.MainBox.GroupBox;
import org.leslie.client.skill.SkillForm.MainBox.GroupBox.CategoryField;
import org.leslie.client.skill.SkillForm.MainBox.GroupBox.DescriptionField;
import org.leslie.client.skill.SkillForm.MainBox.GroupBox.NameField;
import org.leslie.client.skill.SkillForm.MainBox.OkButton;
import org.leslie.shared.security.permission.CreateSkillPermission;
import org.leslie.shared.security.permission.UpdateSkillPermission;
import org.leslie.shared.skill.ISkillService;
import org.leslie.shared.skill.SkillCategoryLookupCall;
import org.leslie.shared.skill.SkillFormData;

@FormData(value = SkillFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class SkillForm extends AbstractForm {

	private Long skillId;

	@FormData
	public Long getSkillId() {
		return skillId;
	}

	@FormData
	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Skill");
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

	public CategoryField getCategoryField() {
		return getFieldByClass(CategoryField.class);
	}

	public DescriptionField getDescriptionField() {
		return getFieldByClass(DescriptionField.class);
	}

	public NameField getNameField() {
		return getFieldByClass(NameField.class);
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
			public class CategoryField extends AbstractSmartField<Long> {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Category");
				}

				@Override
				protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
					return SkillCategoryLookupCall.class;
				}
			}

			@Order(2000)
			public class NameField extends AbstractStringField {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Name");
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
					return 4;
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
			ISkillService service = BEANS.get(ISkillService.class);
			SkillFormData formData = new SkillFormData();
			importFormData(formData);
			formData = service.load(formData);
			importFormData(formData);

			setEnabledPermission(new UpdateSkillPermission());
		}

		@Override
		protected void execStore() {
			ISkillService service = BEANS.get(ISkillService.class);
			SkillFormData formData = new SkillFormData();
			exportFormData(formData);
			formData = service.store(formData);
			importFormData(formData);
		}
	}

	public class NewHandler extends AbstractFormHandler {

		@Override
		protected void execLoad() {
			setEnabledPermission(new CreateSkillPermission());
		}

		@Override
		protected void execStore() {
			ISkillService service = BEANS.get(ISkillService.class);
			SkillFormData formData = new SkillFormData();
			exportFormData(formData);
			formData = service.create(formData);
			importFormData(formData);
		}
	}
}
