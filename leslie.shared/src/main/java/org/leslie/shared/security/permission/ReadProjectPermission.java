package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;
import org.leslie.shared.project.IProjectService;

public class ReadProjectPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public static final int LEVEL_PROJECT = 20;

	private long projectId;

	public ReadProjectPermission() {
		super(ReadProjectPermission.class.getSimpleName() + ".*", LEVEL_PROJECT);
	}

	public ReadProjectPermission(long projectId) {
		super(ReadProjectPermission.class.getSimpleName() + "." + projectId, LEVEL_UNDEFINED);
		this.projectId = projectId;
	}

	public long getProjectId() {
		return projectId;
	}

	@Override
	protected int execCalculateLevel(BasicHierarchyPermission other) {
		int result = LEVEL_ALL;
		if (other instanceof ReadProjectPermission) {
			long projectNr = ((ReadProjectPermission) other).getProjectId();
			ParticipationLevel level = BEANS.get(IProjectService.class).checkParticipationLevel(projectNr);
			if (level != null && level.ordinal() >= ParticipationLevel.VIEWER.ordinal()) {
				result = LEVEL_PROJECT;
			}
		}
		return result;
	}
}
