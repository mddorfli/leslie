package org.leslie.client.permission;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.DataChangeListener;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.client.ClientSession;
import org.leslie.client.permission.PermissionTablePage.Table.AssignToRoleMenu;
import org.leslie.client.permission.PermissionTablePageData.PermissionTableRowData;
import org.leslie.client.util.TableReloadListener;
import org.leslie.shared.DataType;
import org.leslie.shared.role.IRoleService;
import org.leslie.shared.security.PermissionLevel;
import org.leslie.shared.security.permission.ReadAdministrationPermission;
import org.leslie.shared.security.permission.UpdateAdministrationPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageData(PermissionTablePageData.class)
public class PermissionTablePage extends AbstractPageWithTable<PermissionTablePage.Table> {

	private static Logger LOG = LoggerFactory.getLogger(PermissionTablePage.class);

	private boolean m_listenersAdded;

	private Long m_roleNr;

	public PermissionTablePage() {
		super();
		m_listenersAdded = false;
	}

	private void addListeners() {
		if (!m_listenersAdded) {
			IDesktop desktop = ClientSession.get().getDesktop();
			if (getRoleNr() != null) {
				desktop.addDataChangeListener(new TableReloadListener(this), DataType.ROLE_PERMISSION);
			} else {
				desktop.addDataChangeListener(new DataChangeListener() {
					@Override
					public void dataChanged(Object... dataTypes) throws ProcessingException {
						OrderedCollection<IMenu> nodeList = new OrderedCollection<IMenu>();
						AssignToRoleMenu assignToRoleMenu = getTable().getMenuByClass(AssignToRoleMenu.class);
						assignToRoleMenu.injectMenus(nodeList);
						assignToRoleMenu.setChildActions(nodeList.getOrderedList());
					}
				}, DataType.ROLE);
			}
			m_listenersAdded = true;
		}
	}

	@Override
	protected void execLoadData(SearchFilter filter) {
		PermissionTablePageData pageData;
		if (getRoleNr() != null) {
			// get permissions for the current role
			pageData = BEANS.get(IRoleService.class).getPermissionTableData(getRoleNr());

		} else {
			pageData = new PermissionTablePageData();

			// get all permissions
			TreeMap<String, Integer[]> validLevels = new TreeMap<String, Integer[]>();
			Set<Class<? extends Permission>> allPermissions = BEANS.get(IPermissionService.class).getAllPermissionClasses();
			for (Class<? extends Permission> clazz : allPermissions) {
				if (!BasicHierarchyPermission.class.isAssignableFrom(clazz)) {
					continue;
				}
				try {
					BasicHierarchyPermission permissionInstance = (BasicHierarchyPermission) clazz.newInstance();
					List<Integer> levelList = permissionInstance.getValidLevels();
					Integer[] levels = levelList != null ? levelList.toArray(new Integer[] {}) : new Integer[] {};
					validLevels.put(permissionInstance.getClass().getSimpleName(), levels);
				} catch (InstantiationException | IllegalAccessException e) {
					LOG.warn("Could not instantiate permission of type {}", clazz);
				}
			}

			for (Entry<String, Integer[]> entry : validLevels.entrySet()) {
				Integer[] levels = entry.getValue();
				if (levels == null || levels.length == 0) {
					continue;
				}
				PermissionTableRowData row = pageData.addRow();
				row.setName(entry.getKey());
				for (Integer level : levels) {
					switch (PermissionLevel.getInstance(level.intValue())) {
					case LEVEL_NONE:
						row.setNone(true);
						break;
					case LEVEL_OWN:
						row.setOwn(true);
						break;
					case LEVEL_PROJECT:
						row.setProject(true);
						break;
					case LEVEL_ALL:
						row.setAll(true);
						break;
					default:
						break;
					}
				}
			}
		}
		importPageData(pageData);
	}

	@Override
	protected void execPageActivated() throws ProcessingException {
		if (getRoleNr() != null) {
			// displaying only permissions of an existing role
			getTable().getNoneColumn().setDisplayable(false);
			getTable().getOwnColumn().setDisplayable(false);
			getTable().getProjectColumn().setDisplayable(false);
			getTable().getAllColumn().setDisplayable(false);
		} else {
			// no role number means showing global permission properties
			getTable().getLevelColumn().setDisplayable(false);
		}

		addListeners();
	}

	@Override
	protected boolean getConfiguredLeaf() {
		return true;
	}

	@Override
	protected String getConfiguredTitle() {
		return TEXTS.get("Permissions");
	}

	@FormData
	public Long getRoleNr() {
		return m_roleNr;
	}

	@FormData
	public void setRoleNr(Long roleNr) {
		m_roleNr = roleNr;
	}

	@Order(10.0)
	public class Table extends AbstractTable {

		public AllColumn getAllColumn() {
			return getColumnSet().getColumnByClass(AllColumn.class);
		}

		@Override
		protected boolean getConfiguredHeaderVisible() {
			return true;
		}

		public LevelColumn getLevelColumn() {
			return getColumnSet().getColumnByClass(LevelColumn.class);
		}

		public OwnColumn getOwnColumn() {
			return getColumnSet().getColumnByClass(OwnColumn.class);
		}

		public NameColumn getNameColumn() {
			return getColumnSet().getColumnByClass(NameColumn.class);
		}

		public NoneColumn getNoneColumn() {
			return getColumnSet().getColumnByClass(NoneColumn.class);
		}

		public ProjectColumn getProjectColumn() {
			return getColumnSet().getColumnByClass(ProjectColumn.class);
		}

		@Order(10.0)
		public class NameColumn extends AbstractStringColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Name");
			}

			@Override
			protected int getConfiguredSortIndex() {
				return 1;
			}

			@Override
			protected int getConfiguredWidth() {
				return 430;
			}
		}

		@Order(20.0)
		public class LevelColumn extends AbstractSmartColumn<Integer> {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Level");
			}

			@Override
			protected Class<? extends ILookupCall<Integer>> getConfiguredLookupCall() {
				return PermissionLevelLookupCall.class;
			}

			@Override
			protected int getConfiguredWidth() {
				return 90;
			}
		}

		@Order(30.0)
		public class NoneColumn extends AbstractBooleanColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("None");
			}

			@Override
			protected int getConfiguredWidth() {
				return 80;
			}
		}

		@Order(40)
		public class OwnColumn extends AbstractBooleanColumn {
			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Own");
			}

			@Override
			protected int getConfiguredWidth() {
				return 80;
			}
		}

		
		
		@Order(50.0)
		public class ProjectColumn extends AbstractBooleanColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("Project");
			}

			@Override
			protected int getConfiguredWidth() {
				return 80;
			}
		}

		@Order(60.0)
		public class AllColumn extends AbstractBooleanColumn {

			@Override
			protected String getConfiguredHeaderText() {
				return TEXTS.get("All");
			}

			@Override
			protected int getConfiguredWidth() {
				return 80;
			}
		}

		@Order(10.0)
		public class AssignToRoleMenu extends AbstractMenu {

			@Override
			protected void execInitAction() throws ProcessingException {
				setVisiblePermission(new UpdateAdministrationPermission());
			}

			@Override
			protected void execOwnerValueChanged(Object newOwnerValue) {
				// should be at top level
				setVisible(getRoleNr() == null);
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.MultiSelection,
						TableMenuType.SingleSelection);
			}

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("AssignToRole");
			}

			@Override
			protected void injectActionNodesInternal(OrderedCollection<IMenu> nodeList) {
				injectMenus(nodeList);
			}

			private void injectMenus(OrderedCollection<IMenu> nodeList) {
				if (!ACCESS.check(new ReadAdministrationPermission()) || !isVisible()) {
					return;
				}

				try {
					final Map<Long, String> roleMenuItems = BEANS.get(IRoleService.class).getRoleMenuItems();

					for (final Long roleNr : roleMenuItems.keySet()) {
						AbstractMenu roleMenu = new AbstractMenu() {

							@Override
							protected String getConfiguredText() {
								return roleMenuItems.get(roleNr);
							}
						};

						ArrayList<IMenu> subMenus = new ArrayList<IMenu>();
						for (PermissionLevel level : PermissionLevel.values()) {
							if (level == PermissionLevel.LEVEL_UNDEFINED) {
								// skip the first one (undefined)
								continue;
							}

							AbstractMenu subMenu = new AbstractMenu() {

								@Override
								protected void execAction() throws ProcessingException {
									BEANS.get(IRoleService.class).assignPermissions(roleNr,
											getNameColumn().getSelectedValues(), level.getValue());
									ClientSession.get().getDesktop().dataChanged(DataType.ROLE_PERMISSION);
								}

								@Override
								protected void execOwnerValueChanged(Object newOwnerValue) {
									// only show the valid options which apply
									// to the current selection
									boolean enabled = true;
									List<Boolean> values = null;
									if (level.getValue() == BasicHierarchyPermission.LEVEL_NONE) {
										values = getNoneColumn().getSelectedValues();

									} else if (level.getValue() == PermissionLevel.LEVEL_OWN.getValue()) {
										values = getOwnColumn().getSelectedValues();
										
									} else if (level.getValue() == PermissionLevel.LEVEL_PROJECT.getValue()) {
										values = getProjectColumn().getSelectedValues();

									} else if (level.getValue() == BasicHierarchyPermission.LEVEL_ALL) {
										values = getAllColumn().getSelectedValues();
									}
									if (values != null) {
										for (Boolean b : values) {
											if (b == null || b.equals(Boolean.FALSE)) {
												enabled = false;
											}
										}
									}
									setEnabled(enabled);
								}

								@Override
								protected Set<? extends IMenuType> getConfiguredMenuTypes() {
									return CollectionUtility.<IMenuType>hashSet(TableMenuType.MultiSelection,
											TableMenuType.SingleSelection);
								}

								@Override
								protected String getConfiguredText() {
									return level.getNameLK();
								}
							};
							subMenus.add(subMenu);
							subMenu.setParent(roleMenu);
						}
						roleMenu.setChildActions(subMenus);
						nodeList.addLast(roleMenu);
					}
				} catch (ProcessingException e) {
					LOG.error("Error occurred populating menu", e);
				}
			}
		}

		@Order(20.0)
		public class RevokePermissionsMenu extends AbstractMenu {

			@Override
			protected void execAction() throws ProcessingException {
				BEANS.get(IRoleService.class).revokePermissions(getRoleNr(),
						getTable().getNameColumn().getSelectedValues());
				reloadPage();
			}

			@Override
			protected void execOwnerValueChanged(Object newOwnerValue) {
				// only show the remove menu when it is below a parent role
				setVisible(getRoleNr() != null && ACCESS.check(new UpdateAdministrationPermission()));
			}

			@Override
			protected Set<? extends IMenuType> getConfiguredMenuTypes() {
				return CollectionUtility.<IMenuType>hashSet(TableMenuType.MultiSelection,
						TableMenuType.SingleSelection);
			}

			@Override
			protected String getConfiguredText() {
				return TEXTS.get("RevokePermissions");
			}
		}
	}
}
