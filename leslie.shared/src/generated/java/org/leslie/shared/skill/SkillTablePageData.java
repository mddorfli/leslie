package org.leslie.shared.skill;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "org.leslie.client.skill.SkillTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class SkillTablePageData extends AbstractTablePageData {

	private static final long serialVersionUID = 1L;

	@Override
	public SkillTableRowData addRow() {
		return (SkillTableRowData) super.addRow();
	}

	@Override
	public SkillTableRowData addRow(int rowState) {
		return (SkillTableRowData) super.addRow(rowState);
	}

	@Override
	public SkillTableRowData createRow() {
		return new SkillTableRowData();
	}

	@Override
	public Class<? extends AbstractTableRowData> getRowType() {
		return SkillTableRowData.class;
	}

	@Override
	public SkillTableRowData[] getRows() {
		return (SkillTableRowData[]) super.getRows();
	}

	@Override
	public SkillTableRowData rowAt(int index) {
		return (SkillTableRowData) super.rowAt(index);
	}

	public void setRows(SkillTableRowData[] rows) {
		super.setRows(rows);
	}

	public static class SkillTableRowData extends AbstractTableRowData {

		private static final long serialVersionUID = 1L;
		public static final String id = "id";
		public static final String categoryId = "categoryId";
		public static final String category = "category";
		public static final String name = "name";
		public static final String description = "description";
		private Long m_id;
		private Long m_categoryId;
		private String m_category;
		private String m_name;
		private String m_description;

		public Long getId() {
			return m_id;
		}

		public void setId(Long newId) {
			m_id = newId;
		}

		public Long getCategoryId() {
			return m_categoryId;
		}

		public void setCategoryId(Long newCategoryId) {
			m_categoryId = newCategoryId;
		}

		public String getCategory() {
			return m_category;
		}

		public void setCategory(String newCategory) {
			m_category = newCategory;
		}

		public String getName() {
			return m_name;
		}

		public void setName(String newName) {
			m_name = newName;
		}

		public String getDescription() {
			return m_description;
		}

		public void setDescription(String newDescription) {
			m_description = newDescription;
		}
	}
}
