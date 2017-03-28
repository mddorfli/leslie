package org.leslie.client.role;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.leslie.client.ClientSession;
import org.leslie.client.permission.PermissionTablePage;
import org.leslie.shared.DataType;
import org.leslie.shared.role.IRoleService;
import org.leslie.shared.security.permission.UpdateAdministrationPermission;

@PageData(RolePageData.class)
public class RoleTablePage extends AbstractPageWithTable<RoleTablePage.Table> {

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Roles");
	}

	@Override
	protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
		PermissionTablePage childPage = new PermissionTablePage();
		childPage.setRoleNr(getTable().getIdColumn().getValue(row));
		childPage.setLeaf(true);
		return childPage;
	}

	@Override
	protected void execLoadData(SearchFilter filter) {
		importPageData(BEANS.get(IRoleService.class).getRoleTableData());
	}

	@Order(10.0)
	public class Table extends AbstractTable {

		@Override
		protected boolean getConfiguredAutoResizeColumns() {
			return true;
		}

		public IdColumn getIdColumn() {
			return getColumnSet().getColumnByClass(IdColumn.class);
		}

		public NameColumn getNameColumn() {
			return getColumnSet().getColumnByClass(NameColumn.class);
		}

		@Order(10.0)
		public class IdColumn extends AbstractLongColumn {

			@Override
			protected boolean getConfiguredDisplayable() {
				return false;
			}
		}

		@Order(20.0)
		public class NameColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Name");
			}
		}

		@Order(10.0)
		public class NewRoleMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("NewRole_");
			}

			@Override
			protected void execAction() throws ProcessingException {
				RoleForm form = new RoleForm();
				form.startNew();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
					ClientSession.get().getDesktop().dataChanged(DataType.ROLE);
				}
			}

			@Override
			protected void execInitAction() throws ProcessingException {
				setVisiblePermission(new UpdateAdministrationPermission());
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.EmptySpace);
			}
		}

		@Order(20.0)
		public class EditRoleMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("EditRole_");
			}

			@Override
			protected void execAction() throws ProcessingException {
				RoleForm form = new RoleForm();
				form.setRoleId(getTable().getIdColumn().getSelectedValue());
				form.getNameField().setValue(getTable().getNameColumn().getSelectedValue());
				form.startModify();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
					ClientSession.get().getDesktop().dataChanged(DataType.ROLE);
				}
			}

			@Override
			protected void execInitAction() throws ProcessingException {
				setVisiblePermission(new UpdateAdministrationPermission());
			}
		}

		@Order(30.0)
		public class DeleteRoleMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("DeleteRole_");
			}

			@Override
			protected void execAction() throws ProcessingException {
				if (MessageBoxes.showDeleteConfirmationMessage(getTable().getNameColumn().getSelectedValue())) {
					BEANS.get(IRoleService.class).delete(getTable().getIdColumn().getSelectedValue());
					reloadPage();
					ClientSession.get().getDesktop().dataChanged(DataType.ROLE);
				}
			}

			@Override
			protected void execInitAction() throws ProcessingException {
				setVisiblePermission(new UpdateAdministrationPermission());
			}
		}
	}
}
