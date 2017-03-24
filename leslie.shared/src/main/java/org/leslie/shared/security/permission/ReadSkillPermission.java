package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadSkillPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;
	
	public static final int LEVEL_OWN = 10;

	public ReadSkillPermission() {
		super(ReadSkillPermission.class.getSimpleName(), LEVEL_OWN);
	}
}
