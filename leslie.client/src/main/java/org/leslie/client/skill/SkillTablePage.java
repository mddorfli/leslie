package org.leslie.client.skill;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.skill.SkillTablePage.Table;
import org.leslie.client.skill.SkillTablePage.Table.DeleteSkillMenu;
import org.leslie.client.skill.SkillTablePage.Table.EditSkillMenu;
import org.leslie.client.skill.SkillTablePage.Table.NewSkillMenu;
import org.leslie.client.skill.SkillTablePage.Table.SelfAssessMenu;
import org.leslie.shared.security.permission.AssessSkillPermission;
import org.leslie.shared.security.permission.CreateSkillPermission;
import org.leslie.shared.security.permission.UpdateSkillPermission;
import org.leslie.shared.skill.AssessmentCodeType;
import org.leslie.shared.skill.ISkillService;
import org.leslie.shared.skill.ISkillService.SkillPresentation;
import org.leslie.shared.skill.SkillTablePageData;

@Data(SkillTablePageData.class)
public class SkillTablePage extends AbstractPageWithTable<Table> {

	private SkillPresentation presentationType;

	private Long categoryId;

	private Long userId;

	public SkillTablePage(SkillPresentation presentationType) {
		this.presentationType = presentationType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Skills");
	}

	@Override
	protected void execLoadData(SearchFilter filter) {
		SkillTablePageData pageData;
		ISkillService service = BEANS.get(ISkillService.class);
		switch (presentationType) {
		case PERSONAL:
			pageData = service.getPersonalSkillTableData();
			break;
		case ADMIN_CATEGORY:
			pageData = service.getCategorySkillTableData(getCategoryId());
			break;
		case ADMIN:
		default:
			pageData = service.getSkillTableData();
			break;
		}

		importPageData(pageData);
	}

	@Override
	protected boolean getConfiguredLeaf() {
		return true;
	}

	@Override
	protected void execInitPage() {
		switch (presentationType) {
		case PERSONAL:
			getTable().getDescriptionColumn().setDisplayable(false);
			getTable().getMenuByClass(EditSkillMenu.class).setVisibleGranted(false);
			getTable().getMenuByClass(NewSkillMenu.class).setVisibleGranted(false);
			getTable().getMenuByClass(DeleteSkillMenu.class).setVisibleGranted(false);
			break;
		case ADMIN_CATEGORY:
			getTable().getCategoryColumn().setDisplayable(false);
			getTable().getMenuByClass(SelfAssessMenu.class).setVisibleGranted(false);
		case ADMIN:
		default:
			getTable().getAffinityColumn().setDisplayable(false);
			getTable().getSelfAssessmentColumn().setDisplayable(false);
			getTable().getAssessmentColumn().setDisplayable(false);
			getTable().getAssessedByColumn().setDisplayable(false);
			getTable().getLastModifiedColumn().setDisplayable(false);
			getTable().getMenuByClass(SelfAssessMenu.class).setVisibleGranted(false);
			break;
		}
	}

	public class Table extends AbstractTable {

		public CategoryIdColumn getCategoryIdColumn() {
			return getColumnSet().getColumnByClass(CategoryIdColumn.class);
		}

		public CategoryColumn getCategoryColumn() {
			return getColumnSet().getColumnByClass(CategoryColumn.class);
		}

		public DescriptionColumn getDescriptionColumn() {
			return getColumnSet().getColumnByClass(DescriptionColumn.class);
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
			protected int getConfiguredSortIndex() {
				return 0;
			}
		}

		@Order(5000)
		public class DescriptionColumn extends AbstractStringColumn {
			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Description");
			}

			@Override
			protected int getConfiguredWidth() {
				return 500;
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

		@Order(1000)
		public class EditSkillMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
				return TEXTS.get("EditSkill_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new UpdateSkillPermission());
			}

			@Override
			protected void execAction() {
				SkillForm form = new SkillForm();
				form.setSkillId(getIdColumn().getSelectedValue());
				form.startModify();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}
		}

		@Order(2000)
		public class NewSkillMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
				return TEXTS.get("NewSkill_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.EmptySpace);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new CreateSkillPermission());
			}

			@Override
			protected void execAction() {
				SkillForm form = new SkillForm();
				form.startSelfAssessment();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}
		}

		@Order(3000)
		public class DeleteSkillMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
				return TEXTS.get("DeleteSkill_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.MultiSelection);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new UpdateSkillPermission());
			}

			@Override
			protected void execAction() {
				if (MessageBoxes.showDeleteConfirmationMessage(getNameColumn().getSelectedValues())) {
					ISkillService service = BEANS.get(ISkillService.class);
					service.delete(getIdColumn().getSelectedValues());
					reloadPage();
				}
			}
		}

		@Order(4000)
		public class SelfAssessMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
				return TEXTS.get("SelfAssess_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new AssessSkillPermission());
			}

			@Override
			protected void execAction() {
				SkillAssessmentForm form = new SkillAssessmentForm();
				form.setSkillId(getIdColumn().getSelectedValue());
				form.setSkillAssessmentId(getAssessmentIdColumn().getSelectedValue());
				form.startNew();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}
		}

	}
}
