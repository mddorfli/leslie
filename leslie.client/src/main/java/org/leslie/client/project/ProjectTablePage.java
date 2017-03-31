package org.leslie.client.project;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.client.project.ProjectTablePage.Table;
import org.leslie.shared.code.ParticipationCodeType;
import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;
import org.leslie.shared.project.IProjectService;
import org.leslie.shared.security.permission.CreateProjectPermission;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.security.permission.ReadProjectPermission;

@Data(ProjectTablePageData.class)
public class ProjectTablePage extends AbstractPageWithTable<Table> {

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Projects");
	}

	@Override
	protected void execLoadData(SearchFilter filter) {
		importPageData(BEANS.get(IProjectService.class).getProjectTableData(filter));
	}

	@Override
	protected IPage<?> execCreateChildPage(ITableRow row) {
		ProjectNodePage nodePage = new ProjectNodePage();
		nodePage.setProjectId(getTable().getIdColumn().getValue(row));
		return nodePage;
	}

	public class Table extends AbstractTable {

		public ParticipationColumn getParticipationColumn() {
			return getColumnSet().getColumnByClass(ParticipationColumn.class);
		}

		public NameColumn getNameColumn() {
			return getColumnSet().getColumnByClass(NameColumn.class);
		}

		public IdColumn getIdColumn() {
			return getColumnSet().getColumnByClass(IdColumn.class);
		}

		@Order(1000)
		public class IdColumn extends AbstractLongColumn {

			@Override
			protected boolean getConfiguredDisplayable() {
				return false;
			}
		}

		@Order(2000)
		public class NameColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Name");
			}

			@Override
			protected int getConfiguredWidth() {
				return 300;
			}

			@Override
			protected boolean getConfiguredSummary() {
				return true;
			}
		}

		@Order(3000)
		public class ParticipationColumn extends AbstractSmartColumn<ParticipationLevel> {
			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Participation");
			}

			@Override
			protected int getConfiguredWidth() {
				return 150;
			}

			@Override
			protected Class<? extends ICodeType<?, ParticipationLevel>> getConfiguredCodeType() {
				return ParticipationCodeType.class;
			}
		}

		@Order(1000)
		public class NewProjectMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("NewProject_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.EmptySpace);
			}

			@Override
			protected void execAction() {
				ProjectForm form = new ProjectForm();
				form.startNew();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new CreateProjectPermission());
			}
		}

		@Order(2000)
		public class EditProjectMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("EditProject_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected void execAction() {
				ProjectForm form = new ProjectForm();
				form.setProjectId(getIdColumn().getSelectedValue());
				form.startModify();
				form.waitFor();
				if (form.isFormStored()) {
					reloadPage();
				}
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new ReadProjectPermission());
			}
		}

		@Order(3000)
		public class DeleteProjectMenu extends AbstractMenu {

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("DeleteProject_");
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.hashSet(TableMenuType.SingleSelection);
			}

			@Override
			protected void execAction() {
				if (MessageBoxes.showDeleteConfirmationMessage(getNameColumn().getSelectedValue())) {
					BEANS.get(IProjectService.class).deleteProject(getIdColumn().getSelectedValue());
					reloadPage();
				}
			}

			@Override
			protected boolean getConfiguredVisible() {
				return ACCESS.check(new ManageProjectPermission());
			}

			@Override
			protected void execOwnerValueChanged(Object newOwnerValue) {
				ITableRow row = CollectionUtility.firstElement(newOwnerValue);
				setEnabled(ACCESS.check(new ManageProjectPermission(getIdColumn().getValue(row))));
			}
		}
	}
}
