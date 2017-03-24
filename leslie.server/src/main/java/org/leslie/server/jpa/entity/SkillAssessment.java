package org.leslie.server.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "users_x_skill")
@IdClass(SkillAssessmentId.class)
public class SkillAssessment {

	@Id
	private User user;

	@Id
	private Skill skill;

	@Column(name = "self_assessment")
	private Double selfAssessment;

	@Column(name = "self_affinity")
	private Double selfAffinity;

	private Double assessment;

	@ManyToOne
	@JoinColumn(name = "assessed_by")
	private User assessedBy;

	@Column(name = "last_modified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;

	public User getUser() {
		return user;
	}

	void setUser(User user) {
		this.user = user;
	}

	public Skill getSkill() {
		return skill;
	}

	void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Double getSelfAssessment() {
		return selfAssessment;
	}

	public void setSelfAssessment(Double selfAssessment) {
		this.selfAssessment = selfAssessment;
	}

	public Double getSelfAffinity() {
		return selfAffinity;
	}

	public void setSelfAffinity(Double selfAffinity) {
		this.selfAffinity = selfAffinity;
	}

	public Double getAssessment() {
		return assessment;
	}

	public void setAssessment(Double assessment) {
		this.assessment = assessment;
	}

	public User getAssessedBy() {
		return assessedBy;
	}

	public void setAssessedBy(User assessedBy) {
		this.assessedBy = assessedBy;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
