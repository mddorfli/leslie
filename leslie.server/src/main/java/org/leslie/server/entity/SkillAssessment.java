package org.leslie.server.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@NamedQueries({
		@NamedQuery(name = SkillAssessment.QUERY_LATEST_BY_USER_ID_FETCH_ALL, query = ""
				+ "SELECT DISTINCT sa "
				+ "  FROM SkillAssessment sa "
				+ "  LEFT JOIN FETCH sa.user "
				+ "  LEFT JOIN FETCH sa.skill "
				+ "  LEFT JOIN FETCH sa.assessedBy "
				+ " WHERE sa.user.id = :userId "
				+ "   AND sa.id IN (SELECT MAX(sqsa.id) FROM SkillAssessment sqsa GROUP BY sqsa.user, sqsa.skill)"),
		@NamedQuery(name = SkillAssessment.QUERY_HISTORY_BY_SKILL_ID_USER_ID, query = "SELECT sa FROM SkillAssessment sa WHERE sa.user.id = :userId AND sa.skill.id = :skillId "),
})
@MappedClass(value = SkillAssessmentMapping.class)
public class SkillAssessment {

	public static final String QUERY_LATEST_BY_USER_ID_FETCH_ALL = "SkillAssessment.latestByUserIdFetchAll";
	public static final String QUERY_HISTORY_BY_SKILL_ID_USER_ID = "SkillAssessment.historyBySkillIdUserId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@MappedField(readOnly = true, pageFieldName = "assessmentId")
	private long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "skill_id")
	private Skill skill;

	@Column(name = "self_assessment_uid")
	@MappedField
	private Integer selfAssessment;

	@Column(name = "self_affinity_uid")
	@MappedField(pageFieldName = "affinity")
	private Integer selfAffinity;

	@MappedField
	@Column(name = "assessment_uid")
	private Integer assessment;

	@ManyToOne
	@JoinColumn(name = "assessed_by")
	private User assessedBy;

	@Column(name = "last_modified")
	@Temporal(TemporalType.TIMESTAMP)
	@MappedField
	private Date lastModified;

	public long getId() {
		return id;
	}

	void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Integer getSelfAssessment() {
		return selfAssessment;
	}

	public void setSelfAssessment(Integer selfAssessment) {
		this.selfAssessment = selfAssessment;
	}

	public Integer getSelfAffinity() {
		return selfAffinity;
	}

	public void setSelfAffinity(Integer selfAffinity) {
		this.selfAffinity = selfAffinity;
	}

	public void setAssessment(Integer assessment) {
		this.assessment = assessment;
	}

	public Integer getAssessment() {
		return assessment;
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
