package org.leslie.server.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.leslie.server.jpa.mapping.MappedClass;
import org.leslie.server.jpa.mapping.MappedField;

@Entity
@Table(name="skill_category")
@NamedQuery(name=SkillCategory.QUERY_ALL, query="SELECT c FROM SkillCategory c")
@MappedClass
public class SkillCategory {
	
	public static final String QUERY_ALL = "SkillCategory.all";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@MappedField(readOnly=true)
	private long id;

	@MappedField
	private String name;

	public long getId() {
		return id;
	}

	void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
