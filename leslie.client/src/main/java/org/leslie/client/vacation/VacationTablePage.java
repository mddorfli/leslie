package org.leslie.client.vacation;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.activity.ActivityTablePage;
import org.leslie.client.vacation.VacationTablePage.Table;
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
    }
}
