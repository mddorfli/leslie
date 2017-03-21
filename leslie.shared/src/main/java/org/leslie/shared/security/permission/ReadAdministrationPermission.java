package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadAdministrationPermission extends BasicHierarchyPermission {

    private static final long serialVersionUID = 0L;

    public ReadAdministrationPermission() {
	super(ReadAdministrationPermission.class.getSimpleName(), LEVEL_ALL);
    }
}
