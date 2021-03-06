package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;
import org.leslie.shared.project.IProjectService;

public class ManageProjectPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;
	public static final int LEVEL_PROJECT = 20;
	private long projectId;

	public ManageProjectPermission() {
		super(ManageProjectPermission.class.getSimpleName() + ".*", LEVEL_PROJECT);
	}

	public ManageProjectPermission(long projectId) {
		super(ManageProjectPermission.class.getSimpleName() + "." + projectId, LEVEL_UNDEFINED);
		this.projectId = projectId;
	}

	public long getProjectId() {
		return projectId;
	}

	@Override
	protected int execCalculateLevel(BasicHierarchyPermission other) {
		int result = LEVEL_ALL;
		if (other instanceof ManageProjectPermission) {
			long projectNr = ((ManageProjectPermission) other).getProjectId();
			ParticipationLevel level = BEANS.get(IProjectService.class).checkParticipationLevel(projectNr);
			if (level == ParticipationLevel.MANAGER) {
				result = LEVEL_PROJECT;
			}
		}
		return result;
	}
}
