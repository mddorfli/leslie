package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadProjectPermission extends BasicHierarchyPermission {

    private static final long serialVersionUID = 1L;

    public static final int LEVEL_PROJECT = 10;

    public ReadProjectPermission() {
	super(ReadProjectPermission.class.getSimpleName(), LEVEL_ALL);
    }
}
