package leslie.org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

import leslie.org.leslie.shared.security.IGlobalPermission;

public class CreateVacationPermission extends BasicHierarchyPermission implements IGlobalPermission {

    private static final long serialVersionUID = 1L;

    public CreateVacationPermission() {
	super(CreateVacationPermission.class.getSimpleName());
    }
}
