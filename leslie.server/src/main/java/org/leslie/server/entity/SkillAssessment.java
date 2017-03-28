package org.leslie.server.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.leslie.server.entity.mapping.SkillAssessmentMapping;
import org.leslie.server.mapping.MappedClass;
import org.leslie.server.mapping.MappedField;

@Entity
@Table(name = "users_x_skill")
@IdClass(SkillAssessmentId.class)
@NamedQueries({
		@NamedQuery(name = SkillAssessment.QUERY_LATEST_BY_USER_FETCH_ALL, query = ""
				+ "SELECT DISTINCT sa "
				+ "  FROM SkillAssessment sa "
				+ "  LEFT JOIN FETCH sa.user "
				+ "  LEFT JOIN FETCH sa.skill "
				+ "  LEFT JOIN FETCH sa.assessedBy "
				+ " WHERE sa.user.id = :userId "
				+ "   AND sa.lastModified IN (SELECT MAX(sqsa.lastModified) FROM SkillAssessment sqsa GROUP BY sqsa.user, sqsa.skill)"),
})
@MappedClass(value = SkillAssessmentMapping.class)
public class SkillAssessment {

	public static final String QUERY_LATEST_BY_USER_FETCH_ALL = "SkillAssessment.latestByUserFetchAll";

	@Id
	private User user;

	@Id
	private Skill skill;

	@Column(name = "self_assessment")
	@MappedField
	private Double selfAssessment;

	@Column(name = "self_affinity")
	@MappedField(pageFieldName = "affinity")
	private Double selfAffinity;

	@MappedField
	private Double assessment;

	@ManyToOne
	@JoinColumn(name = "assessed_by")
	private User assessedBy;

	@Column(name = "last_modified")
	@Temporal(TemporalType.TIMESTAMP)
	@MappedField
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
