package org.leslie.server.jpa;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String version;

    @OneToMany(mappedBy = "project")
    private Collection<UserProjectAssignment> userAssignments;

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

    public Collection<UserProjectAssignment> getUserAssignments() {
	return userAssignments;
    }

    public void setUserAssignments(Collection<UserProjectAssignment> userAssignments) {
	this.userAssignments = userAssignments;
    }

}
