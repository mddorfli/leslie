package org.leslie.server.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.leslie.shared.code.ParticipationCodeType.Participation;

@Entity
@Table(name = "user_x_project")
@IdClass(ProjectAssignmentId.class)
public class ProjectAssignment {

    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "participation_level_uid")
    private Participation participationLevel;

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public Participation getParticipationLevel() {
	return participationLevel;
    }

    public void setParticipationLevel(Participation participationLevel) {
	this.participationLevel = participationLevel;
    }

}
