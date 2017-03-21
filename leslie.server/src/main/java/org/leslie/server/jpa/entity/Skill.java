package org.leslie.server.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.leslie.server.jpa.mapping.MappedClass;
import org.leslie.server.jpa.mapping.MappedField;
import org.leslie.server.jpa.mapping.impl.SkillMapping;

@Entity
@NamedQueries({
	@NamedQuery(name=Skill.QUERY_ALL, query="SELECT s FROM Skill s"),
	@NamedQuery(name=Skill.QUERY_ALL_FETCH_CATEGORY, query="SELECT s FROM Skill s JOIN FETCH s.category "),
	@NamedQuery(name=Skill.QUERY_BY_CATEGORY_ID, query="SELECT s FROM Skill s JOIN s.category c WHERE c.id = :categoryId "),
})
@MappedClass(SkillMapping.class)
public class Skill {
	
	public static final String QUERY_ALL = "Skill.all";
	public static final String QUERY_ALL_FETCH_CATEGORY = "Skill.allFetchCategory";
	public static final String QUERY_BY_CATEGORY_ID = "Skill.byCategoryIdFetchCategory";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@MappedField(readOnly=true)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private SkillCategory category;
	
	@MappedField
	private String name;
	
	@MappedField
	private String description;

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

	
	
}
