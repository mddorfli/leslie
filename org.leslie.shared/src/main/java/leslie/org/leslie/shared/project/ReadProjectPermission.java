package leslie.org.leslie.shared.project;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadProjectPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public ReadProjectPermission() {
		super(ReadProjectPermission.class.getSimpleName());
	}
}
