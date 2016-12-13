package leslie.org.leslie.shared.security.permission;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public class ReadProjectPermission extends BasicHierarchyPermission {

    private static final long serialVersionUID = 1L;

    public static final int LEVEL_PROJECT = 10;

    public ReadProjectPermission(long projectId) {
	super(ReadProjectPermission.class.getSimpleName() + "." + projectId, LEVEL_PROJECT);
    }

    @Override
    protected int execCalculateLevel(BasicHierarchyPermission other) {
	return super.execCalculateLevel(other);
    }
}
