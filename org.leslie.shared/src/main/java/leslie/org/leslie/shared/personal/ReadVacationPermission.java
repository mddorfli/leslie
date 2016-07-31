package leslie.org.leslie.shared.personal;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadVacationPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public ReadVacationPermission() {
		super(ReadVacationPermission.class.getSimpleName());
	}
}
