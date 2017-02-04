package org.leslie.server.jpa.entity;

import java.io.Serializable;

public class ProjectAssignmentId implements Serializable {

    private static final long serialVersionUID = -5627449679048798001L;

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

}
