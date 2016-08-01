package leslie.org.leslie.shared.security;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class CreateProjectPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public CreateProjectPermission() {
		super(CreateProjectPermission.class.getSimpleName());
	}
}
