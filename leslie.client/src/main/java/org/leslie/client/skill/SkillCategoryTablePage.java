package org.leslie.client.skill;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.skill.SkillCategoryTablePage.Table;
import org.leslie.shared.security.permission.CreateSkillPermission;
import org.leslie.shared.security.permission.ReadSkillPermission;
import org.leslie.shared.security.permission.UpdateSkillPermission;
import org.leslie.shared.skill.ISkillCategoryService;
import org.leslie.shared.skill.ISkillService.SkillPresentation;
import org.leslie.shared.skill.SkillCategoryTablePageData;

@Data(SkillCategoryTablePageData.class)
public class SkillCategoryTablePage extends AbstractPageWithTable<Table> {

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("SkillCategories");
	}

	@Override
	protected void execLoadData(SearchFilter filter) {
		importPageData(BEANS.get(ISkillCategoryService.class).getSkillCategoryTableData());
	}

	@Override
	protected void execInitPage() {
		setVisibleGranted(ACCESS.getLevel(new ReadSkillPermission()) == ReadSkillPermission.LEVEL_ALL);
	}

	@Override
	protected IPage<?> execCreateChildPage(ITableRow row) {
		SkillTablePage childPage = new SkillTablePage(SkillPresentation.ADMIN_CATEGORY);
		Long categoryId = getTable().getIdColumn().getValue(row);
		childPage.setCategoryId(categoryId);
		return childPage;
	}

	public class Table extends AbstractTable {

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

		@Order(2000)
		public class NameColumn extends AbstractStringColumn {
			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Name");
			}

			@Override
			protected int getConfiguredWidth() {
				return 200;
			}
		}

		@Order(1000)
		public class EditCategoryMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
				return TEXTS.get("EditCategory_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.getLevel(new UpdateSkillPermission()) == UpdateSkillPermission.LEVEL_ALL;
			}

			@Override
			protected void execAction() {
				SkillCategoryForm form = new SkillCategoryForm();
				form.setCategoryId(getIdColumn().getSelectedValue());
				form.startModify();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}
		}

		@Order(2000)
		public class NewCategoryMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
				return TEXTS.get("NewCategory_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.EmptySpace);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.getLevel(new CreateSkillPermission()) == CreateSkillPermission.LEVEL_ALL;
			}

			@Override
			protected void execAction() {
				SkillCategoryForm form = new SkillCategoryForm();
				form.startNew();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}
		}

		@Order(3000)
		public class DeleteCategoryMenu extends AbstractMenu {
			@Override
			protected String getConfiguredText() {
				return TEXTS.get("DeleteCategory_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.MultiSelection);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.getLevel(new UpdateSkillPermission()) == UpdateSkillPermission.LEVEL_ALL;
			}

			@Override
			protected void execAction() {
				if (MessageBoxes.showDeleteConfirmationMessage(getNameColumn().getSelectedValues())) {
					ISkillCategoryService service = BEANS.get(ISkillCategoryService.class);
					service.delete(getIdColumn().getSelectedValues());
					reloadPage();
				}
			}
		}

	}
}
