package org.leslie.client.skill;

import org.eclipse.scout.rt.client.dto.Data;
import org.leslie.shared.skill.ISkillService.SkillPresentation;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.skill.SkillTablePage.Table;
import org.leslie.shared.skill.ISkillService;
import org.leslie.shared.skill.SkillTablePageData;

@Data(SkillTablePageData.class)
public class SkillTablePage extends AbstractPageWithTable<Table> {

	private SkillPresentation presentationType;

	private Long categoryId;

	public SkillTablePage(SkillPresentation presentationType) {
		this.presentationType = presentationType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Skills");
	}

	@Override
	protected void execLoadData(SearchFilter filter) {
		SkillTablePageData pageData;
		switch(presentationType) {
		case ADMIN_CATEGORY:
			pageData = BEANS.get(ISkillService.class).getSkillTableData(getCategoryId());
			break;
		case ADMIN:
		default:
			pageData = BEANS.get(ISkillService.class).getSkillTableData();
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
		switch(presentationType) {
		case ADMIN:
			break;
		case ADMIN_CATEGORY:
			getTable().getCategoryColumn().setDisplayable(false);
			break;
		default:
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

	}
}
