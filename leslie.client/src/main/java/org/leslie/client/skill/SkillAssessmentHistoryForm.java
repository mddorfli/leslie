package org.leslie.client.skill;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCloseButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.leslie.client.skill.SkillAssessmentHistoryForm.MainBox.CloseButton;
import org.leslie.client.skill.SkillAssessmentHistoryForm.MainBox.GroupBox;
import org.leslie.client.skill.SkillAssessmentHistoryForm.MainBox.GroupBox.HistoryField;
import org.leslie.shared.skill.AssessmentCodeType;
import org.leslie.shared.skill.ISkillAssessmentService;
import org.leslie.shared.skill.SkillAssessmentHistoryFormData;

@FormData(value = SkillAssessmentHistoryFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class SkillAssessmentHistoryForm extends AbstractForm {

	private Long skillAssessmentId;

	private Long userId;

	@FormData
	public Long getSkillAssessmentId() {
		return skillAssessmentId;
	}

	@FormData
	public void setSkillAssessmentId(Long skillAssessmentId) {
		this.skillAssessmentId = skillAssessmentId;
	}

	@FormData
	public Long getUserId() {
		return userId;
	}

	@FormData
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("AssessmentHistory");
	}

	public void startView() {
		startInternalExclusive(new ViewHandler());
	}

	public CloseButton getCloseButton() {
		return getFieldByClass(CloseButton.class);
	}

	public MainBox getMainBox() {
		return getFieldByClass(MainBox.class);
	}

	public GroupBox getGroupBox() {
		return getFieldByClass(GroupBox.class);
	}

	public HistoryField getHistoryField() {
		return getFieldByClass(HistoryField.class);
	}

	@Order(1000)
	public class MainBox extends AbstractGroupBox {

		@Order(1000)
		public class GroupBox extends AbstractGroupBox {

			@Order(1000)
			public class HistoryField extends AbstractTableField<HistoryField.Table> {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("History");
				}

				@Override
				protected int getConfiguredGridH() {
					return 6;
				}

				public class Table extends AbstractTable {

					@Override
					protected void execDecorateCell(Cell view, ITableRow row, IColumn<?> col) {
						Long editingUserId = getModifiedByIdColumn().getValue(row);
						if (editingUserId.equals(getUserId())
								&& (col.getColumnId().equals(getSelfAssessmentColumn().getColumnId())
										|| col.getColumnId().equals(getAffinityColumn().getColumnId()))) {
							// it was (probably) a self-assessment.
							view.setFont(FontSpec.parse("BOLD"));
						}
						if (editingUserId.equals(getAssessedByIdColumn().getValue(row))
								&& (col.getColumnId().equals(getAssessmentColumn().getColumnId())
										|| col.getColumnId().equals(getAssessedByColumn().getColumnId()))) {
							// it was (probably) an assessment.
							view.setFont(FontSpec.parse("BOLD"));
						}
					}

					public CategoryIdColumn getCategoryIdColumn() {
						return getColumnSet().getColumnByClass(CategoryIdColumn.class);
					}

					public CategoryColumn getCategoryColumn() {
						return getColumnSet().getColumnByClass(CategoryColumn.class);
					}

					public SelfAssessmentColumn getSelfAssessmentColumn() {
						return getColumnSet().getColumnByClass(SelfAssessmentColumn.class);
					}

					public AffinityColumn getAffinityColumn() {
						return getColumnSet().getColumnByClass(AffinityColumn.class);
					}

					public AssessmentColumn getAssessmentColumn() {
						return getColumnSet().getColumnByClass(AssessmentColumn.class);
					}

					public AssessedByColumn getAssessedByColumn() {
						return getColumnSet().getColumnByClass(AssessedByColumn.class);
					}

					public LastModifiedColumn getLastModifiedColumn() {
						return getColumnSet().getColumnByClass(LastModifiedColumn.class);
					}

					public AssessedByIdColumn getAssessedByIdColumn() {
						return getColumnSet().getColumnByClass(AssessedByIdColumn.class);
					}

					public AssessmentIdColumn getAssessmentIdColumn() {
						return getColumnSet().getColumnByClass(AssessmentIdColumn.class);
					}

					public ModifiedByIdColumn getModifiedByIdColumn() {
						return getColumnSet().getColumnByClass(ModifiedByIdColumn.class);
					}

					public ModifiedByColumn getModifiedByColumn() {
						return getColumnSet().getColumnByClass(ModifiedByColumn.class);
					}

					public NameColumn getNameColumn() {
						return getColumnSet().getColumnByClass(NameColumn.class);
					}

					public IdColumn getIdColumn() {
						return getColumnSet().getColumnByClass(IdColumn.class);
					}

					@Order(1000)
					public class IdColumn extends AbstractLongColumn {
						@Override
						protected boolean getConfiguredDisplayable() {
							return false;
						}
					}

					@Order(1500)
					public class AssessmentIdColumn extends AbstractLongColumn {
						@Override
						protected boolean getConfiguredDisplayable() {
							return false;
						}
					}

					@Order(2000)
					public class CategoryIdColumn extends AbstractLongColumn {
						@Override
						protected boolean getConfiguredDisplayable() {
							return false;
						}
					}

					@Order(3000)
					public class CategoryColumn extends AbstractStringColumn {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("Category");
						}

						@Override
						protected int getConfiguredWidth() {
							return 165;
						}
					}

					@Order(4000)
					public class NameColumn extends AbstractStringColumn {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("Name");
						}

						@Override
						protected int getConfiguredWidth() {
							return 200;
						}

						@Override
						protected boolean getConfiguredSummary() {
							return true;
						}
					}

					@Order(6000)
					public class SelfAssessmentColumn extends AbstractSmartColumn<Integer> {

						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("SelfAssessment");
						}

						@Override
						protected Class<? extends ICodeType<?, Integer>> getConfiguredCodeType() {
							return AssessmentCodeType.class;
						}

						@Override
						protected int getConfiguredWidth() {
							return 130;
						}
					}

					@Order(7000)
					public class AffinityColumn extends AbstractSmartColumn<Integer> {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("Affinity");
						}

						@Override
						protected Class<? extends ICodeType<?, Integer>> getConfiguredCodeType() {
							return AssessmentCodeType.class;
						}

						@Override
						protected int getConfiguredWidth() {
							return 100;
						}
					}

					@Order(8000)
					public class AssessmentColumn extends AbstractSmartColumn<Integer> {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("Assessment");
						}

						@Override
						protected Class<? extends ICodeType<?, Integer>> getConfiguredCodeType() {
							return AssessmentCodeType.class;
						}

						@Override
						protected int getConfiguredWidth() {
							return 100;
						}
					}

					@Order(8500)
					public class AssessedByIdColumn extends AbstractLongColumn {
						@Override
						protected boolean getConfiguredDisplayable() {
							return false;
						}
					}

					@Order(9000)
					public class AssessedByColumn extends AbstractStringColumn {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("AssessedBy");
						}

						@Override
						protected int getConfiguredWidth() {
							return 150;
						}
					}

					@Order(10000)
					public class LastModifiedColumn extends AbstractDateTimeColumn {

						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("LastModified");
						}

						@Override
						protected int getConfiguredWidth() {
							return 120;
						}
					}

					@Order(11000)
					public class ModifiedByIdColumn extends AbstractLongColumn {

						@Override
						protected boolean getConfiguredDisplayable() {
							return false;
						}
					}

					@Order(12000)
					public class ModifiedByColumn extends AbstractStringColumn {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("ModifiedBy");
						}

						@Override
						protected int getConfiguredWidth() {
							return 100;
						}
					}

				}
			}

		}

		@Order(100000)
		public class CloseButton extends AbstractCloseButton {
		}

	}

	public class ViewHandler extends AbstractFormHandler {

		@Override
		protected void execLoad() {
			SkillAssessmentHistoryFormData formData = new SkillAssessmentHistoryFormData();
			exportFormData(formData);
			formData = BEANS.get(ISkillAssessmentService.class).loadHistory(formData);
			importFormData(formData);
		}
	}
}
