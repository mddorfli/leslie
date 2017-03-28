package org.leslie.server.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.leslie.server.entity.mapping.SkillMapping;
import org.leslie.server.mapping.MappedClass;
import org.leslie.server.mapping.MappedField;

@Entity
@NamedQueries({
		@NamedQuery(name = Skill.QUERY_ALL, query = "SELECT s FROM Skill s"),
		@NamedQuery(name = Skill.QUERY_ALL_FETCH_CATEGORY, query = "SELECT s FROM Skill s LEFT JOIN FETCH s.category "),
		@NamedQuery(name = Skill.QUERY_IN_CATEGORY_IDS, query = "SELECT s FROM Skill s JOIN s.category c WHERE c.id IN :categoryIds "),
		@NamedQuery(name = Skill.QUERY_BY_USER_ID_FETCH_CATEGORY, query = ""
				+ "SELECT DISTINCT s "
				+ "  FROM Skill s "
				+ "  LEFT JOIN FETCH s.category "
				+ "  JOIN s.assessments a "
				+ " WHERE a.user.id = :userId ")
})
@MappedClass(SkillMapping.class)
public class Skill {

	public static final String QUERY_ALL = "Skill.all";
	public static final String QUERY_ALL_FETCH_CATEGORY = "Skill.allFetchCategory";
	public static final String QUERY_IN_CATEGORY_IDS = "Skill.inCategoryIds";
	public static final String QUERY_BY_USER_ID_FETCH_CATEGORY = "Skill.byUserId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@MappedField(readOnly = true, formFieldName = "skillId")
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private SkillCategory category;

	@MappedField
	private String name;

	@MappedField
	private String description;

	@OneToMany(mappedBy = "skill", orphanRemoval = true)
	private Collection<SkillAssessment> assessments;

	public long getId() {
		return id;
	}

	void setId(long id) {
		this.id = id;
	}

	public SkillCategory getCategory() {
		return category;
	}

	public void setCategory(SkillCategory category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<SkillAssessment> getAssessments() {
		return assessments;
	}

	public void setAssessments(Collection<SkillAssessment> assessments) {
		this.assessments = assessments;
	}

}
