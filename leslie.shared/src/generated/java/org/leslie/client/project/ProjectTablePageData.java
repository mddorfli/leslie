package org.leslie.client.project;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;
import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "org.leslie.client.project.ProjectTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class ProjectTablePageData extends AbstractTablePageData {

	private static final long serialVersionUID = 1L;

	@Override
	public ProjectTableRowData addRow() {
		return (ProjectTableRowData) super.addRow();
	}

	@Override
	public ProjectTableRowData addRow(int rowState) {
		return (ProjectTableRowData) super.addRow(rowState);
	}

	@Override
	public ProjectTableRowData createRow() {
		return new ProjectTableRowData();
	}

	@Override
	public Class<? extends AbstractTableRowData> getRowType() {
		return ProjectTableRowData.class;
	}

	@Override
	public ProjectTableRowData[] getRows() {
		return (ProjectTableRowData[]) super.getRows();
	}

	@Override
	public ProjectTableRowData rowAt(int index) {
		return (ProjectTableRowData) super.rowAt(index);
	}

	public void setRows(ProjectTableRowData[] rows) {
		super.setRows(rows);
	}

	public static class ProjectTableRowData extends AbstractTableRowData {

		private static final long serialVersionUID = 1L;
		public static final String id = "id";
		public static final String name = "name";
		public static final String participation = "participation";
		private Long m_id;
		private String m_name;
		private ParticipationLevel m_participation;

		public Long getId() {
			return m_id;
		}

		public void setId(Long newId) {
			m_id = newId;
		}

		public String getName() {
			return m_name;
		}

		public void setName(String newName) {
			m_name = newName;
		}

		public ParticipationLevel getParticipation() {
			return m_participation;
		}

		public void setParticipation(ParticipationLevel newParticipation) {
			m_participation = newParticipation;
		}
	}
}
