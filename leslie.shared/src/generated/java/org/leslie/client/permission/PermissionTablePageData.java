package org.leslie.client.permission;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "org.leslie.client.permission.PermissionTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PermissionTablePageData extends AbstractTablePageData {

	private static final long serialVersionUID = 1L;

	@Override
	public PermissionTableRowData addRow() {
		return (PermissionTableRowData) super.addRow();
	}

	@Override
	public PermissionTableRowData addRow(int rowState) {
		return (PermissionTableRowData) super.addRow(rowState);
	}

	@Override
	public PermissionTableRowData createRow() {
		return new PermissionTableRowData();
	}

	@Override
	public Class<? extends AbstractTableRowData> getRowType() {
		return PermissionTableRowData.class;
	}

	@Override
	public PermissionTableRowData[] getRows() {
		return (PermissionTableRowData[]) super.getRows();
	}

	@Override
	public PermissionTableRowData rowAt(int index) {
		return (PermissionTableRowData) super.rowAt(index);
	}

	public void setRows(PermissionTableRowData[] rows) {
		super.setRows(rows);
	}

	public static class PermissionTableRowData extends AbstractTableRowData {

		private static final long serialVersionUID = 1L;
		public static final String name = "name";
		public static final String level = "level";
		public static final String none = "none";
		public static final String own = "own";
		public static final String project = "project";
		public static final String all = "all";
		private String m_name;
		private Integer m_level;
		private Boolean m_none;
		private Boolean m_own;
		private Boolean m_project;
		private Boolean m_all;

		public String getName() {
			return m_name;
		}

		public void setName(String newName) {
			m_name = newName;
		}

		public Integer getLevel() {
			return m_level;
		}

		public void setLevel(Integer newLevel) {
			m_level = newLevel;
		}

		public Boolean getNone() {
			return m_none;
		}

		public void setNone(Boolean newNone) {
			m_none = newNone;
		}

		public Boolean getOwn() {
			return m_own;
		}

		public void setOwn(Boolean newOwn) {
			m_own = newOwn;
		}

		public Boolean getProject() {
			return m_project;
		}

		public void setProject(Boolean newProject) {
			m_project = newProject;
		}

		public Boolean getAll() {
			return m_all;
		}

		public void setAll(Boolean newAll) {
			m_all = newAll;
		}
	}
}
