package leslie.org.leslie.shared.security;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class UpdateProjectPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public UpdateProjectPermission() {
		super(UpdateProjectPermission.class.getSimpleName());
	}
}
