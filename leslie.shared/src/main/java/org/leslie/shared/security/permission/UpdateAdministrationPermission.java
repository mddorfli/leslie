package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class UpdateAdministrationPermission extends BasicHierarchyPermission {

    private static final long serialVersionUID = 0L;

    public UpdateAdministrationPermission() {
	super(UpdateAdministrationPermission.class.getSimpleName(), LEVEL_ALL);
    }
}
