package org.leslie.client.project;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.activity.ActivityTablePage;
import org.leslie.client.project.ProjectResourcesTablePage.Table;
import org.leslie.client.project.ProjectResourcesTablePage.Table.AddResourceMenu;
import org.leslie.client.project.ProjectResourcesTablePage.Table.RemoveResourceMenu;
import org.leslie.client.project.ProjectResourcesTablePage.Table.UpdateResourceMenu;
import org.leslie.shared.activity.ProjectResourcesTablePageData;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.security.permission.ReadProjectPermission;

@Data(ProjectResourcesTablePageData.class)
public class ProjectResourcesTablePage extends ActivityTablePage<Table> {

    private Long projectId;

    @FormData
    public Long getProjectId() {
	return projectId;
    }

    @FormData
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
    protected void execPageActivated() {
	getTable().getMenuByClass(AddResourceMenu.class)
		.setVisiblePermission(new ManageProjectPermission(getProjectId()));
	getTable().getMenuByClass(RemoveResourceMenu.class)
		.setVisiblePermission(new ManageProjectPermission(getProjectId()));
	getTable().getMenuByClass(UpdateResourceMenu.class)
		.setVisiblePermission(new ReadProjectPermission(getProjectId()));
    }

    @Override
    protected void execLoadData(SearchFilter filter) {
	importPageData(BEANS.get(IProjectActivityService.class).getProjectActivityTableData(filter, getProjectId()));
    }

    public class Table extends ActivityTablePage<Table>.Table {

	public PercentageColumn getPercentageColumn() {
	    return getColumnSet().getColumnByClass(PercentageColumn.class);
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

	    @Override
	    protected int getConfiguredMultiplier() {
		return 100;
	    }

	    @Override
	    protected boolean getConfiguredPercent() {
		return true;
	    }
	}

	@Order(1000)
	public class AddResourceMenu extends AbstractMenu {
	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("AddResource_");
	    }

	    @Override
	    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
		return CollectionUtility.hashSet(TableMenuType.EmptySpace);
	    }

	    @Override
	    protected void execAction() {
		ProjectActivityForm form = new ProjectActivityForm();
		form.setProjectId(getProjectId());
		form.startNew();
		form.waitFor();
		if (form.isFormStored()) {
		    reloadPage();
		}
	    }
	}

	@Order(2000)
	public class RemoveResourceMenu extends AbstractMenu {
	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("RemoveResource_");
	    }

	    @Override
	    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
		return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.MultiSelection);
	    }

	    @Override
	    protected void execAction() {
		if (MessageBoxes.showDeleteConfirmationMessage(TEXTS.get("Resources"), null)) {
		    BEANS.get(IProjectActivityService.class).remove(getActivityIdColumn().getSelectedValues());
		    reloadPage();
		}
	    }
	}

	@Order(3000)
	public class UpdateResourceMenu extends AbstractMenu {
	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("UpdateResource_");
	    }

	    @Override
	    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
		return CollectionUtility.hashSet(TableMenuType.SingleSelection);
	    }

	    @Override
	    protected void execAction() {
		ProjectActivityForm form = new ProjectActivityForm();
		form.setActivityId(getActivityIdColumn().getSelectedValue());
		form.setProjectId(getProjectId());
		form.startModify();
		form.waitFor();
		if (form.isFormStored()) {
		    reloadPage();
		}
	    }
	}

    }
}
