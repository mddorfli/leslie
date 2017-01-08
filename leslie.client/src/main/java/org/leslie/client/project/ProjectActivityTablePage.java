package org.leslie.client.project;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.activity.ActivityTablePage;
import org.leslie.client.project.ProjectActivityTablePage.Table;
import org.leslie.shared.activity.IActivityService;
import org.leslie.shared.activity.ProjectActivityTablePageData;

@Data(ProjectActivityTablePageData.class)
public class ProjectActivityTablePage extends ActivityTablePage<Table> {

    private Long projectId;

    public Long getProjectId() {
	return projectId;
    }

    public void setProjectId(Long projectId) {
	this.projectId = projectId;
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Resources");
    }

    @Override
    protected boolean getConfiguredLeaf() {
	return true;
    }

    @Override
    protected void execLoadData(SearchFilter filter) {
	importPageData(BEANS.get(IActivityService.class).getProjectActivityTableData(filter, getProjectId()));
    }

    public class Table extends ActivityTablePage<Table>.Table {

	public PercentageColumn getPercentageColumn() {
	    return getColumnSet().getColumnByClass(PercentageColumn.class);
	}

	public ProjectColumn getProjectColumn() {
	    return getColumnSet().getColumnByClass(ProjectColumn.class);
	}

	public ProjectIdColumn getProjectIdColumn() {
	    return getColumnSet().getColumnByClass(ProjectIdColumn.class);
	}

	@Order(4000)
	public class ProjectIdColumn extends AbstractLongColumn {

	    @Override
	    protected boolean getConfiguredDisplayable() {
		return false;
	    }
	}

	@Order(4500)
	public class ProjectColumn extends AbstractStringColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("Project");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

	@Order(5000)
	public class PercentageColumn extends AbstractBigDecimalColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("Percentage");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

    }
}
