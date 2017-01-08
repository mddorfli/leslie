package org.leslie.shared.activity;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "org.leslie.client.project.ProjectActivityTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class ProjectActivityTablePageData extends ActivityTablePageData {

    private static final long serialVersionUID = 1L;

    @Override
    public ProjectActivityTableRowData addRow() {
	return (ProjectActivityTableRowData) super.addRow();
    }

    @Override
    public ProjectActivityTableRowData addRow(int rowState) {
	return (ProjectActivityTableRowData) super.addRow(rowState);
    }

    @Override
    public ProjectActivityTableRowData createRow() {
	return new ProjectActivityTableRowData();
    }

    @Override
    public Class<? extends AbstractTableRowData> getRowType() {
	return ProjectActivityTableRowData.class;
    }

    @Override
    public ProjectActivityTableRowData[] getRows() {
	return (ProjectActivityTableRowData[]) super.getRows();
    }

    @Override
    public ProjectActivityTableRowData rowAt(int index) {
	return (ProjectActivityTableRowData) super.rowAt(index);
    }

    public void setRows(ProjectActivityTableRowData[] rows) {
	super.setRows(rows);
    }

    public static class ProjectActivityTableRowData extends ActivityTableRowData {

	private static final long serialVersionUID = 1L;
	public static final String projectId = "projectId";
	public static final String project = "project";
	public static final String percentage = "percentage";
	private Long m_projectId;
	private String m_project;
	private BigDecimal m_percentage;

	public Long getProjectId() {
	    return m_projectId;
	}

	public void setProjectId(Long newProjectId) {
	    m_projectId = newProjectId;
	}

	public String getProject() {
	    return m_project;
	}

	public void setProject(String newProject) {
	    m_project = newProject;
	}

	public BigDecimal getPercentage() {
	    return m_percentage;
	}

	public void setPercentage(BigDecimal newPercentage) {
	    m_percentage = newPercentage;
	}
    }
}