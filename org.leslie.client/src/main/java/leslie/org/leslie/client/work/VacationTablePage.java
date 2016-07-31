package leslie.org.leslie.client.work;

import java.text.MessageFormat;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractIntegerColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import leslie.org.leslie.client.work.VacationTablePage.Table;
import leslie.org.leslie.shared.work.IVacationService;
import leslie.org.leslie.shared.work.VacationsTablePageData;

@Data(VacationsTablePageData.class)
public class VacationTablePage extends AbstractPageWithTable<Table> {

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Vacations");
	}

	@Override
	protected void execLoadData(SearchFilter filter) {
		importPageData(BEANS.get(IVacationService.class).getVacationsTableData(filter));
	}

	@Override
	protected boolean getConfiguredLeaf() {
		return true;
	}

	public class Table extends AbstractTable {

		public FromColumn getFromColumn() {
			return getColumnSet().getColumnByClass(FromColumn.class);
		}

		public ApprovedColumn getApprovedColumn() {
			return getColumnSet().getColumnByClass(ApprovedColumn.class);
		}

		public ApprovedByColumn getApprovedByColumn() {
			return getColumnSet().getColumnByClass(ApprovedByColumn.class);
		}

		public DaysColumn getDaysColumn() {
			return getColumnSet().getColumnByClass(DaysColumn.class);
		}

		public ToColumn getToColumn() {
			return getColumnSet().getColumnByClass(ToColumn.class);
		}

		public IdColumn getIdColumn() {
			return getColumnSet().getColumnByClass(IdColumn.class);
		}

		public class IdColumn extends AbstractLongColumn {

			@Override
			protected boolean getConfiguredDisplayable() {
				return false;
			}
		}

		@Order(10)
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

		@Order(20)
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

		@Order(30)
		public class DaysColumn extends AbstractIntegerColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Days");
			}

			@Override
			protected int getConfiguredWidth() {
				return 100;
			}
		}

		@Order(40)
		public class ApprovedColumn extends AbstractBooleanColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Approved");
			}

			@Override
			protected int getConfiguredWidth() {
				return 100;
			}
		}

		@Order(50)
		public class ApprovedByColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("ApprovedBy");
			}

			@Override
			protected int getConfiguredWidth() {
				return 140;
			}
		}

		@Order(1000)
		public class NewVacationRequestMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("NewVacationRequest_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.EmptySpace);
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
		public class EditVacationRequestMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("EditVacationRequest_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected void execAction() {
				VacationForm form = new VacationForm();
				form.setId(getIdColumn().getSelectedValue());
				form.startModify();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}
		}

		@Order(3000)
		public class CancelVacationRequestMenu extends AbstractMenu {

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
				setEnabled(row != null && getApprovedColumn().getValue(row) == false);
			}

			@Override
			protected void execAction() {
				String display = MessageFormat.format("{0} - {1}", getFromColumn().getSelectedDisplayText(),
						getToColumn().getSelectedDisplayText());
				if (MessageBoxes.showDeleteConfirmationMessage(TEXTS.get("Vacations"), display)) {
					BEANS.get(IVacationService.class).delete(getIdColumn().getSelectedValue());
					reloadPage();
				}
			}
		}

	}
}
