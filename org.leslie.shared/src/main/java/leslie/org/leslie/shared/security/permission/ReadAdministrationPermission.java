package leslie.org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

import leslie.org.leslie.shared.security.IGlobalPermission;

public class ReadAdministrationPermission extends BasicHierarchyPermission implements IGlobalPermission {

    private static final long serialVersionUID = 0L;

    public ReadAdministrationPermission() {
	super("ReadAdministration", LEVEL_ALL);
    }
}
