package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class CreateSkillPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public CreateSkillPermission() {
		super(CreateSkillPermission.class.getSimpleName(), LEVEL_ALL);
	}
}
