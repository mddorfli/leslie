package org.leslie.client.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.project.ProjectResourcePlanTablePage.Table;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectResourcePlanSearchFormData;

public class ProjectResourcePlanTablePage extends AbstractPageWithTable<Table> {

    private Long projectId;

    private List<IColumn<?>> injectedColumns;

    public Long getProjectId() {
	return projectId;
    }

    public void setProjectId(Long projectId) {
	this.projectId = projectId;
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("ResourcePlan");
    }

    @Override
    protected boolean getConfiguredLeaf() {
	return true;
    }

    @Override
    protected Class<? extends ISearchForm> getConfiguredSearchForm() {
	return ProjectResourcePlanSearchForm.class;
    }

    @Override
    protected void execLoadData(SearchFilter filter) {
	ProjectResourcePlanSearchFormData searchForm = (ProjectResourcePlanSearchFormData) filter.getFormData();
	Object[][] data = BEANS.get(IProjectActivityService.class).loadResourceData(projectId, searchForm);

	injectedColumns = new ArrayList<>();
	Object[] headers = data[0];
	for (int i = 2; i < headers.length; i++) {
	    final String columnId = String.valueOf(i);
	    final String headerText = (String) headers[i];
	    IColumn<?> column = new AbstractBigDecimalColumn() {
		@Override
		public String getColumnId() {
		    return columnId;
		}

		@Override
		protected String getConfiguredHeaderText() {
		    return headerText;
		}

		@Override
		protected int getConfiguredWidth() {
		    return 120;
		}
	    };
	    injectedColumns.add(column);
	}
	getTable().resetColumnConfiguration();

	if (data == null || data.length <= 1) {
	    // no data
	    importTableData(new Object[0][]);
	} else {
	    // use everything after the 1st row
	    importTableData(Arrays.copyOfRange(data, 1, data.length));
	}
    }

    @Override
    protected void execInitSearchForm() {
	ProjectResourcePlanSearchForm searchForm = (ProjectResourcePlanSearchForm) getSearchFormInternal();
	searchForm.startSearch();
    }

    public class Table extends AbstractTable {

	public ResourcesColumn getResourcesColumn() {
	    return getColumnSet().getColumnByClass(ResourcesColumn.class);
	}

	public DateColumn getDateColumn() {
	    return getColumnSet().getColumnByClass(DateColumn.class);
	}

	@Order(1000)
	public class DateColumn extends AbstractDateColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("Date");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }

	    @Override
	    protected int getConfiguredSortIndex() {
		return 0;
	    }
	}

	@Order(2000)
	public class ResourcesColumn extends AbstractBigDecimalColumn {

	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("Resources");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

	@Override
	protected void injectColumnsInternal(OrderedCollection<IColumn<?>> columns) {
	    if (injectedColumns != null) {
		columns.addAllOrdered(injectedColumns);
	    }
	}
    }
}
