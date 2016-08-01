package leslie.org.leslie.shared.security;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class UpdateAdministrationPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 0L;

	public UpdateAdministrationPermission() {
		super("UpdateAdministration", LEVEL_ALL);
	}
}
