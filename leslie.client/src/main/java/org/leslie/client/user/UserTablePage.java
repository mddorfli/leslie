package org.leslie.client.user;

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
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
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
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.ClientSession;
import org.leslie.client.user.UserTablePage.Table.AddUserMenu;
import org.leslie.client.user.UserTablePage.Table.DeleteUserMenu;
import org.leslie.client.user.UserTablePage.Table.EditUserMenu;
import org.leslie.client.user.UserTablePage.Table.NewUserMenu;
import org.leslie.client.user.UserTablePage.Table.RemoveUserMenu;
import org.leslie.shared.code.ParticipationCodeType;
import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;
import org.leslie.shared.project.IProjectService;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.security.permission.UpdateAdministrationPermission;
import org.leslie.shared.user.IUserService;
import org.leslie.shared.user.IUserService.UserPresentation;

@PageData(UserPageData.class)
public class UserTablePage extends AbstractPageWithTable<UserTablePage.Table> {

	private UserPresentation presentationType;

	private Long projectId;

	public UserTablePage(UserPresentation presentationType) {
		this.presentationType = presentationType;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	@Override
	protected void execPageActivated() {
		switch (presentationType) {
		case ADMINISTRATION:
			getTable().getDisplayNameColumn().setDisplayable(false);
			getTable().getParticipationLevelColumn().setDisplayable(false);

			getTable().getMenuByClass(AddUserMenu.class).setVisibleGranted(false);
			getTable().getMenuByClass(RemoveUserMenu.class).setVisibleGranted(false);
			break;

		case PROJECT:
			boolean pmEnabled = ACCESS.check(new ManageProjectPermission(getProjectId()));
			getTable().getUsernameColumn().setDisplayable(false);
			getTable().getFirstNameColumn().setDisplayable(false);
			getTable().getLastNameColumn().setDisplayable(false);
			getTable().getEmailColumn().setDisplayable(false);
			getTable().getLastLoginColumn().setDisplayable(false);
			getTable().getLoginAttemptsColumn().setDisplayable(false);
			getTable().getBlockedColumn().setDisplayable(false);

			getTable().getMenuByClass(NewUserMenu.class).setVisibleGranted(false);
			getTable().getMenuByClass(EditUserMenu.class).setVisibleGranted(false);
			getTable().getMenuByClass(DeleteUserMenu.class).setVisibleGranted(false);
			getTable().getMenuByClass(AddUserMenu.class).setEnabled(pmEnabled);
			getTable().getMenuByClass(RemoveUserMenu.class).setEnabled(pmEnabled);
			break;
		}
	}

	@Override
	protected void execLoadData(SearchFilter filter) throws ProcessingException {
		switch (presentationType) {
		case ADMINISTRATION:
			importPageData(BEANS.get(IUserService.class).getAdministrationUserTableData());
			break;
		case PROJECT:
			importPageData(BEANS.get(IUserService.class).getProjectUserTableData(getProjectId()));
			break;
		}
	}

	@Override
	protected boolean getConfiguredLeaf() {
		return true;
	}

	@Override
	protected String getConfiguredTitle() {
		switch (presentationType) {
		case ADMINISTRATION:
		default:
			return TEXTS.get("Users");
		case PROJECT:
			return TEXTS.get("Members");
		}
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

		public DisplayNameColumn getDisplayNameColumn() {
			return getColumnSet().getColumnByClass(DisplayNameColumn.class);
		}

		public ParticipationLevelColumn getParticipationLevelColumn() {
			return getColumnSet().getColumnByClass(ParticipationLevelColumn.class);
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

		@Order(45)
		public class DisplayNameColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Name");
			}

			@Override
			protected int getConfiguredWidth() {
				return 200;
			}
		}

		@Order(47)
		public class ParticipationLevelColumn extends AbstractSmartColumn<ParticipationLevel> {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Participation");
			}

			@Override
			protected Class<? extends ICodeType<?, ParticipationLevel>> getConfiguredCodeType() {
				return ParticipationCodeType.class;
			}

			@Override
			protected int getConfiguredWidth() {
				return 120;
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
			protected String getConfiguredText() {
				return TEXTS.get("NewUser_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.EmptySpace);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new UpdateAdministrationPermission());
			}

			@Override
			protected void execAction() throws ProcessingException {
				UserForm form = new UserForm();
				form.getPasswordField().setMandatory(true);
				form.startNew();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}

		}

		@Order(20.0)
		public class EditUserMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("EditUser_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new UpdateAdministrationPermission());
			}

			@Override
			protected void execAction() throws ProcessingException {
				UserForm form = new UserForm();
				form.setUserId(getTable().getIdColumn().getSelectedValue());
				form.startModify();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}
		}

		@Order(30.0)
		public class DeleteUserMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("DeleteUser_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new UpdateAdministrationPermission());
			}

			@Override
			protected void execAction() throws ProcessingException {
				if (MessageBoxes.showDeleteConfirmationMessage(getTable().getUsernameColumn().getSelectedValue())) {
					BEANS.get(IUserService.class).delete(getTable().getIdColumn().getSelectedValue());
					reloadPage();
				}
			}
		}

		@Order(2000)
		public class AddUserMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("AddUser_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.EmptySpace);
			}

			@Override
			protected void execAction() {
				UserSelectionForm form = new UserSelectionForm();
				form.setProjectId(getProjectId());
				form.startNew();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new ManageProjectPermission());
			}
		}

		@Order(3000)
		public class RemoveUserMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("RemoveUser_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected void execAction() {
				if (MessageBoxes.showDeleteConfirmationMessage(getDisplayNameColumn().getSelectedDisplayText())) {
					BEANS.get(IProjectService.class).removeUser(getProjectId(), getIdColumn().getSelectedValue());
					reloadPage();
				}
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new ManageProjectPermission());
			}
		}
	}
}
