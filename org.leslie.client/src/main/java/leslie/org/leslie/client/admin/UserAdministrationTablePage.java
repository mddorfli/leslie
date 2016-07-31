package leslie.org.leslie.client.admin;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractIntegerColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import leslie.org.leslie.client.ClientSession;
import leslie.org.leslie.shared.admin.IAdministrationOutlineService;
import leslie.org.leslie.shared.admin.IUserAdministrationService;
import leslie.org.leslie.shared.admin.UserAdministrationPageData;
import leslie.org.leslie.shared.security.UpdateAdministrationPermission;

@PageData(UserAdministrationPageData.class)
public class UserAdministrationTablePage extends AbstractPageWithTable<UserAdministrationTablePage.Table> {

	@Override
	protected void execLoadData(SearchFilter filter) throws ProcessingException {
		importPageData(BEANS.get(IAdministrationOutlineService.class).getUserTableData());
	}

	@Override
	protected boolean getConfiguredLeaf() {
		return true;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Users");
	}

	@Order(10.0)
	public class Table extends AbstractTable {

		@Override
		protected void execDecorateRow(ITableRow row) throws ProcessingException {
			if (CompareUtility.equals(getUsernameColumn().getValue(row), ClientSession.get().getUserId())) {
				row.setFont(FontSpec.parse("BOLD"));
			}
		}

		@Override
		protected Class<? extends IMenu> getConfiguredDefaultMenu() {
			return EditUserMenu.class;
		}

		public EmailColumn getEmailColumn() {
			return getColumnSet().getColumnByClass(EmailColumn.class);
		}

		public FirstNameColumn getFirstNameColumn() {
			return getColumnSet().getColumnByClass(FirstNameColumn.class);
		}

		public IdColumn getIdColumn() {
			return getColumnSet().getColumnByClass(IdColumn.class);
		}

		public LastLoginColumn getLastLoginColumn() {
			return getColumnSet().getColumnByClass(LastLoginColumn.class);
		}

		public LastNameColumn getLastNameColumn() {
			return getColumnSet().getColumnByClass(LastNameColumn.class);
		}

		public BlockedColumn getBlockedColumn() {
			return getColumnSet().getColumnByClass(BlockedColumn.class);
		}

		public LoginAttemptsColumn getLoginAttemptsColumn() {
			return getColumnSet().getColumnByClass(LoginAttemptsColumn.class);
		}

		public UsernameColumn getUsernameColumn() {
			return getColumnSet().getColumnByClass(UsernameColumn.class);
		}
		@Order(10.0)
		public class IdColumn extends AbstractLongColumn {

			@Override
			protected boolean getConfiguredDisplayable() {
				return false;
			}
		}

		@Order(20.0)
		public class UsernameColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Username");
			}

			@Override
			protected int getConfiguredWidth() {
				return 114;
			}
		}

		@Order(30.0)
		public class FirstNameColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("FirstName");
			}

			@Override
			protected int getConfiguredWidth() {
				return 152;
			}
		}

		@Order(40)
		public class LastNameColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("LastName");
			}

			@Override
			protected int getConfiguredWidth() {
				return 185;
			}
		}

		@Order(50.0)
		public class EmailColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Email");
			}

			@Override
			protected int getConfiguredWidth() {
				return 217;
			}
		}

		@Order(60.0)
		public class LastLoginColumn extends AbstractDateTimeColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("LastLogin");
			}

			@Override
			protected int getConfiguredWidth() {
				return 108;
			}
		}

		@Order(70)
		public class LoginAttemptsColumn extends AbstractIntegerColumn {
			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("LoginAttempts");
			}

			@Override
			protected int getConfiguredWidth() {
				return 121;
			}
		}

		@Order(80.0)
		public class BlockedColumn extends AbstractBooleanColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Blocked");
			}

			@Override
			protected int getConfiguredWidth() {
				return 93;
			}
		}

		@Order(10.0)
		public class NewUserMenu extends AbstractMenu {

			@Override
			protected void execAction() throws ProcessingException {
				UserAdministrationForm form = new UserAdministrationForm();
				form.getPasswordField().setMandatory(true);
				form.startNew();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.EmptySpace);
			}

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("NewUser_");
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new UpdateAdministrationPermission());
			}
		}

		@Order(20.0)
		public class EditUserMenu extends AbstractMenu {

			@Override
			protected void execAction() throws ProcessingException {
				UserAdministrationForm form = new UserAdministrationForm();
				form.setUserNr(getTable().getIdColumn().getSelectedValue());
				form.startModify();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("EditUser_");
			}
		}

		@Order(30.0)
		public class DeleteUserMenu extends AbstractMenu {

			@Override
			protected void execAction() throws ProcessingException {
				if (MessageBoxes.showDeleteConfirmationMessage(getTable().getUsernameColumn().getSelectedValue())) {
					BEANS.get(IUserAdministrationService.class).delete(getTable().getIdColumn().getSelectedValue());
					reloadPage();
				}
			}

			@Override
			protected void execInitAction() throws ProcessingException {
				setVisiblePermission(new UpdateAdministrationPermission());
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("DeleteUser_");
			}
		}
	}
}
