package org.leslie.server.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;

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

    @Column(name = "participation_level_uid")
    @Convert(converter = ParticipationIntegerConverter.class)
    private ParticipationLevel participationLevel;

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

    public ParticipationLevel getParticipationLevel() {
	return participationLevel;
    }

    public void setParticipationLevel(ParticipationLevel participationLevel) {
	this.participationLevel = participationLevel;
    }

}
