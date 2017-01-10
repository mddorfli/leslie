package org.leslie.client.activity;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.leslie.shared.activity.ActivityTablePageData;

@Data(ActivityTablePageData.class)
public abstract class ActivityTablePage<T extends ActivityTablePage<T>.Table>
	extends AbstractPageWithTable<T> {

    public class Table extends AbstractTable {

	public ActivityTablePage<?>.Table.UserIdColumn getUserIdColumn() {
	    return getColumnSet().getColumnByClass(UserIdColumn.class);
	}

	public ActivityTablePage<?>.Table.UserColumn getUserColumn() {
	    return getColumnSet().getColumnByClass(UserColumn.class);
	}

	public ActivityTablePage<?>.Table.ActivityIdColumn getActivityIdColumn() {
	    return getColumnSet().getColumnByClass(ActivityIdColumn.class);
	}

	public ActivityTablePage<?>.Table.ToColumn getToColumn() {
	    return getColumnSet().getColumnByClass(ToColumn.class);
	}

	public ActivityTablePage<?>.Table.FromColumn getFromColumn() {
	    return getColumnSet().getColumnByClass(FromColumn.class);
	}

	@Order(0)
	public class ActivityIdColumn extends AbstractLongColumn {
	    @Override
	    protected boolean getConfiguredDisplayable() {
		return false;
	    }
	}

	@Order(1000)
	public class UserIdColumn extends AbstractLongColumn {
	    @Override
	    protected boolean getConfiguredDisplayable() {
		return false;
	    }
	}

	@Order(1500)
	public class UserColumn extends AbstractStringColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("User");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

	@Order(2000)
	public class FromColumn extends AbstractDateColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("From");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

	@Order(3000)
	public class ToColumn extends AbstractDateColumn {
	    @Override
	    protected String getConfiguredHeaderText() {
		return TEXTS.get("To");
	    }

	    @Override
	    protected int getConfiguredWidth() {
		return 100;
	    }
	}

    }
}
