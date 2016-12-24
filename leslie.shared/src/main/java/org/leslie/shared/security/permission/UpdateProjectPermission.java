package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class UpdateProjectPermission extends BasicHierarchyPermission {

    private static final long serialVersionUID = 1L;
    public static final int LEVEL_PROJECT = 10;

    public UpdateProjectPermission() {
	super(UpdateProjectPermission.class.getSimpleName(), LEVEL_ALL);
    }
}
