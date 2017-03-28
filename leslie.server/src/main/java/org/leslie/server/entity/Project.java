package org.leslie.server.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.leslie.server.mapping.MappedClass;
import org.leslie.server.mapping.MappedField;

@MappedClass
@Entity
@NamedQueries({
	@NamedQuery(name = Project.QUERY_ALL, query = "SELECT p FROM Project p"),
	@NamedQuery(name = Project.QUERY_BY_USER, query = "SELECT p FROM Project p JOIN p.userAssignments ua WHERE ua.user = :user "),
})
public class Project {

    public static final String QUERY_ALL = "Project.all";
    public static final String QUERY_BY_USER = "Project.byUser";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MappedField(readOnly = true, formFieldName = "projectId")
    private long id;

    @MappedField
    private String name;

    @OneToMany(mappedBy = "project")
    private Collection<ProjectAssignment> userAssignments;

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

    public Collection<ProjectAssignment> getUserAssignments() {
	return userAssignments;
    }

    public void setUserAssignments(Collection<ProjectAssignment> userAssignments) {
	this.userAssignments = userAssignments;
    }

}
