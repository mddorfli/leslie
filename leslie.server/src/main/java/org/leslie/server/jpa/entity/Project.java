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

import org.leslie.server.jpa.AccessLevel;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String version;

    @ElementCollection
    @CollectionTable(name = "user_x_project")
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private Map<User, AccessLevel> userAssignments;

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

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    public Map<User, AccessLevel> getUserAssignments() {
	return userAssignments;
    }

    public void setUserAssignments(Map<User, AccessLevel> userAssignments) {
	this.userAssignments = userAssignments;
    }

}
