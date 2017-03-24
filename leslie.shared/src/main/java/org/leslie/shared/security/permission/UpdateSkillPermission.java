package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class UpdateSkillPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public UpdateSkillPermission() {
		super(UpdateSkillPermission.class.getSimpleName(), LEVEL_ALL);
	}

}
