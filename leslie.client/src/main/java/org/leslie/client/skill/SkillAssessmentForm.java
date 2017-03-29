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
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.leslie.client.skill.SkillAssessmentForm.MainBox.CancelButton;
import org.leslie.client.skill.SkillAssessmentForm.MainBox.GroupBox;
import org.leslie.client.skill.SkillAssessmentForm.MainBox.GroupBox.SelfAffinityField;
import org.leslie.client.skill.SkillAssessmentForm.MainBox.GroupBox.SelfAssessmentField;
import org.leslie.client.skill.SkillAssessmentForm.MainBox.GroupBox.SkillDescriptionField;
import org.leslie.client.skill.SkillAssessmentForm.MainBox.GroupBox.SkillNameField;
import org.leslie.client.skill.SkillAssessmentForm.MainBox.OkButton;
import org.leslie.shared.skill.AssessmentCodeType;
import org.leslie.shared.skill.ISkillAssessmentService;
import org.leslie.shared.skill.SkillAssessmentFormData;

@FormData(value = SkillAssessmentFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class SkillAssessmentForm extends AbstractForm {

	private Long skillId;
	private Long skillAssessmentId;

	@FormData
	public Long getSkillId() {
		return skillId;
	}

	@FormData
	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	@FormData
	public Long getSkillAssessmentId() {
		return skillAssessmentId;
	}

	@FormData
	public void setSkillAssessmentId(Long skillAssessmentId) {
		this.skillAssessmentId = skillAssessmentId;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("SkillSelfAssessment");
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

	public SelfAssessmentField getSelfAssessmentField() {
		return getFieldByClass(SelfAssessmentField.class);
	}

	public SkillNameField getSkillNameField() {
		return getFieldByClass(SkillNameField.class);
	}

	public SkillDescriptionField getSkillDescriptionField() {
		return getFieldByClass(SkillDescriptionField.class);
	}

	public SelfAffinityField getSelfAffinityField() {
		return getFieldByClass(SelfAffinityField.class);
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
			public class SkillNameField extends AbstractStringField {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Skill");
				}

				@Override
				protected boolean getConfiguredEnabled() {
					return false;
				}
			}

			@Order(2000)
			public class SkillDescriptionField extends AbstractStringField {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Description");
				}

				@Override
				protected int getConfiguredGridH() {
					return 3;
				}

				@Override
				protected boolean getConfiguredMultilineText() {
					return true;
				}

				@Override
				protected boolean getConfiguredWrapText() {
					return true;
				}

				@Override
				protected boolean getConfiguredEnabled() {
					return false;
				}

			}

			@Order(3000)
			public class SelfAssessmentField extends AbstractSmartField<Integer> {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("SelfAssessment");
				}

				@Override
				protected Class<? extends ICodeType<?, Integer>> getConfiguredCodeType() {
					return AssessmentCodeType.class;
				}

				@Override
				protected boolean getConfiguredMandatory() {
					return true;
				}
			}

			@Order(4000)
			public class SelfAffinityField extends AbstractSmartField<Integer> {
				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("Affinity");
				}

				@Override
				protected Class<? extends ICodeType<?, Integer>> getConfiguredCodeType() {
					return AssessmentCodeType.class;
				}

				@Override
				protected boolean getConfiguredMandatory() {
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

	public class NewHandler extends AbstractFormHandler {

		@Override
		protected void execLoad() {
			ISkillAssessmentService service = BEANS.get(ISkillAssessmentService.class);
			SkillAssessmentFormData formData = new SkillAssessmentFormData();
			exportFormData(formData);
			formData = service.load(formData);
			importFormData(formData);
		}

		@Override
		protected void execStore() {
			ISkillAssessmentService service = BEANS.get(ISkillAssessmentService.class);
			SkillAssessmentFormData formData = new SkillAssessmentFormData();
			exportFormData(formData);
			service.store(formData);
			importFormData(formData);
		}
	}
}
