package leslie.org.leslie.shared.security;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadAdministrationPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 0L;

	public ReadAdministrationPermission() {
		super("ReadAdministration", LEVEL_ALL);
	}
}
