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

import org.leslie.server.jpa.mapping.FieldMapping;
import org.leslie.shared.code.ParticipationCodeType.Participation;

@Entity
public class Project {

    @FieldMapping(readOnly = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @FieldMapping
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
