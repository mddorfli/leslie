package org.leslie.client.vacation;

import java.util.Date;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.activity.ActivityTablePage;
import org.leslie.client.vacation.VacationTablePage.Table;
import org.leslie.shared.security.permission.ApproveVacationPermission;
import org.leslie.shared.security.permission.RequestVacationPermission;
import org.leslie.shared.user.IUserService;
import org.leslie.shared.vacation.IVacationService;
import org.leslie.shared.vacation.VacationTablePageData;

@Data(VacationTablePageData.class)
public class VacationTablePage extends ActivityTablePage<Table> {

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Vacations");
    }

    @Override
    protected void execLoadData(SearchFilter filter) {
	importPageData(BEANS.get(IVacationService.class).getVacationTableData(filter));
    }

    @Override
    protected boolean getConfiguredLeaf() {
	return true;
    }

    @Override
    protected boolean getConfiguredExpanded() {
	return true;
    }

    public class Table extends ActivityTablePage<Table>.Table {

	public ApprovedByColumn getApprovedByColumn() {
	    return getColumnSet().getColumnByClass(ApprovedByColumn.class);
	}

	public DescriptionColumn getDescriptionColumn() {
	    return getColumnSet().getColumnByClass(DescriptionColumn.class);
	}

	public ApprovedByIdColumn getApprovedByIdColumn() {
	    return getColumnSet().getColumnByClass(ApprovedByIdColumn.class);
	}

	@Order(1000)
	public class ApprovedByIdColumn extends AbstractLongColumn {

	    @Override
	    protected boolean getConfiguredDisplayable() {
		return false;
	    }
	}

	@Order(2000)
	public class ApprovedByColumn extends AbstractStringColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("ApprovedBy");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

	@Order(3000)
	public class DescriptionColumn extends AbstractStringColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("Description");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

	@Order(1000)
	public class RequestVacationMenu extends AbstractMenu {
	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("RequestVacation_");
	    }

	    @Override
	    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
		return CollectionUtility.hashSet(TableMenuType.EmptySpace);
	    }

	    @Override
	    protected boolean getConfiguredVisible() {
		return ACCESS.check(new RequestVacationPermission());
	    }

	    @Override
	    protected void execAction() {
		VacationForm form = new VacationForm();
		form.startNew();
		form.waitFor();
		if (form.isFormStored()) {
		    reloadPage();
		}
	    }
	}

	@Order(2000)
	public class UpdateVacationMenu extends AbstractMenu {
	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("UpdateVacation_");
	    }

	    @Override
	    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
		return CollectionUtility.hashSet(TableMenuType.SingleSelection);
	    }

	    @Override
	    protected void execAction() {
		VacationForm form = new VacationForm();
		form.setActivityId(getActivityIdColumn().getSelectedValue());
		form.startModify();
		form.waitFor();
		if (form.isFormStored()) {
		    reloadPage();
		}
	    }
	}

	@Order(3000)
	public class ApproveRequestMenu extends AbstractMenu {
	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("ApproveVacation");
	    }

	    @Override
	    protected boolean getConfiguredVisible() {
		return ACCESS.check(new ApproveVacationPermission());
	    }

	    @Override
	    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
		return CollectionUtility.hashSet(TableMenuType.SingleSelection);
	    }

	    @Override
	    protected void execOwnerValueChanged(Object newOwnerValue) {
		ITableRow row = CollectionUtility.firstElement(newOwnerValue);
		setEnabled(getApprovedByIdColumn().getValue(row) == null);
	    }

	    @Override
	    protected void execAction() {
		BEANS.get(IVacationService.class).approveVacation(getActivityIdColumn().getSelectedValue());
		reloadPage();
	    }
	}

	@Order(4000)
	public class CancelVacationMenu extends AbstractMenu {

	    @Override
	    protected String getConfiguredText() {
		return TEXTS.get("CancelVacationRequest_");
	    }

	    @Override
	    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
		return CollectionUtility.hashSet(TableMenuType.SingleSelection);
	    }

	    @Override
	    protected void execOwnerValueChanged(Object newOwnerValue) {
		ITableRow row = CollectionUtility.firstElement(newOwnerValue);
		Long userNr = BEANS.get(IUserService.class).getCurrentUserNr();
		setEnabled(ACCESS.check(new ApproveVacationPermission())
			|| (userNr.equals(getUserIdColumn().getValue(row))
				&& getFromColumn().getValue(row).before(new Date())));
	    }

	    @Override
	    protected void execAction() {
		if (MessageBoxes.showDeleteConfirmationMessage(TEXTS.get("Vacations"), null)) {
		    BEANS.get(IVacationService.class).remove(getActivityIdColumn().getSelectedValues());
		    reloadPage();
		}
	    }
	}

    }
}
