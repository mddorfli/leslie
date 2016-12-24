package leslie.org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadVacationPermission extends BasicHierarchyPermission {

    private static final long serialVersionUID = 1L;

    public ReadVacationPermission() {
	super(ReadVacationPermission.class.getSimpleName());
    }
}