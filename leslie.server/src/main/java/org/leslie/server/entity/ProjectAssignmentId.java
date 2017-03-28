package org.leslie.server.entity;

import java.io.Serializable;
import java.util.Arrays;

public class ProjectAssignmentId implements Serializable {

	private static final long serialVersionUID = 1L;

	private long user;

	private long project;

	public ProjectAssignmentId() {
	}

	public ProjectAssignmentId(long userId, long projectId) {
		user = userId;
		project = projectId;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public long getProject() {
		return project;
	}

	public void setProject(long project) {
		this.project = project;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ProjectAssignmentId)) {
			return false;
		}
		ProjectAssignmentId other = (ProjectAssignmentId) obj;
		return other.getProject() == getProject() && other.getUser() == getUser();
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new long[] { user, project });
	}
}
