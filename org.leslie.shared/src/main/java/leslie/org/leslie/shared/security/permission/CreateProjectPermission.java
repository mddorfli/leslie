package leslie.org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

import leslie.org.leslie.shared.security.IGlobalPermission;

public class CreateProjectPermission extends BasicHierarchyPermission implements IGlobalPermission {

    private static final long serialVersionUID = 1L;

    public CreateProjectPermission() {
	super(CreateProjectPermission.class.getSimpleName());
    }
}
