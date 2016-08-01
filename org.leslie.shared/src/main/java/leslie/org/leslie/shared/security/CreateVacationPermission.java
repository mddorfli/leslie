package leslie.org.leslie.shared.security;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class CreateVacationPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public CreateVacationPermission() {
		super(CreateVacationPermission.class.getSimpleName());
	}
}
