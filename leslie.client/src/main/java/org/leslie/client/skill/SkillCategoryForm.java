package org.leslie.client.skill;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.skill.SkillCategoryForm.MainBox.CancelButton;
import org.leslie.client.skill.SkillCategoryForm.MainBox.GroupBox;
import org.leslie.client.skill.SkillCategoryForm.MainBox.GroupBox.NameField;
import org.leslie.client.skill.SkillCategoryForm.MainBox.OkButton;
import org.leslie.shared.security.permission.CreateSkillPermission;
import org.leslie.shared.security.permission.UpdateSkillPermission;
import org.leslie.shared.skill.ISkillCategoryService;
import org.leslie.shared.skill.SkillCategoryFormData;

@FormData(value = SkillCategoryFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class SkillCategoryForm extends AbstractForm {

	private Long categoryId;

	@FormData
	public Long getCategoryId() {
		return categoryId;
	}

	@FormData
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Category");
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
			public class NameField extends AbstractStringField {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Name");
				}

				@Override
				protected int getConfiguredMaxLength() {
					return 128;
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
			ISkillCategoryService service = BEANS.get(ISkillCategoryService.class);
			SkillCategoryFormData formData = new SkillCategoryFormData();
			exportFormData(formData);
			formData = service.load(formData);
			importFormData(formData);

			setEnabledGranted(ACCESS.getLevel(new UpdateSkillPermission()) == UpdateSkillPermission.LEVEL_ALL);
		}

		@Override
		protected void execStore() {
			ISkillCategoryService service = BEANS.get(ISkillCategoryService.class);
			SkillCategoryFormData formData = new SkillCategoryFormData();
			exportFormData(formData);
			formData = service.store(formData);
			importFormData(formData);
		}
	}

	public class NewHandler extends AbstractFormHandler {

		@Override
		protected void execLoad() {
			setEnabledGranted(ACCESS.getLevel(new CreateSkillPermission()) == CreateSkillPermission.LEVEL_ALL);
		}

		@Override
		protected void execStore() {
			ISkillCategoryService service = BEANS.get(ISkillCategoryService.class);
			SkillCategoryFormData formData = new SkillCategoryFormData();
			exportFormData(formData);
			formData = service.create(formData);
			importFormData(formData);
		}
	}
}
