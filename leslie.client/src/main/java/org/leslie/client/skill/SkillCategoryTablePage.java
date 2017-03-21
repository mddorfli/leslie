package org.leslie.client.skill;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.skill.SkillCategoryTablePage.Table;
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
	}
}
