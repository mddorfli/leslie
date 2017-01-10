package org.leslie.server.jpa.entity;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQuery;

import org.leslie.server.jpa.mapping.ClassDataMapping;
import org.leslie.server.jpa.mapping.FieldDataMapping;
import org.leslie.shared.code.ParticipationCodeType.Participation;

@ClassDataMapping
@Entity
@NamedQuery(name = Project.QUERY_ALL, query = "SELECT p FROM Project p")
public class Project {

    public static final String QUERY_ALL = "Project.all";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @FieldDataMapping(readOnly = true, formFieldName = "projectId")
    private long id;

    @FieldDataMapping
    private String name;

    @ElementCollection
    @CollectionTable(name = "user_x_project")
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "participation_level_uid")
    @Enumerated(EnumType.STRING)
    private Map<User, Participation> userAssignments;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Map<User, Participation> getUserAssignments() {
	return userAssignments;
    }

    public void setUserAssignments(Map<User, Participation> userAssignments) {
	this.userAssignments = userAssignments;
    }

}
