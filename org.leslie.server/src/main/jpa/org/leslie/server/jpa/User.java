package org.leslie.server.jpa;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.scout.rt.platform.util.StringUtility;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;

    private boolean blocked;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "pw_salt")
    private String passwordSalt;

    @Column(name = "pw_hash")
    private String passwordHash;

    @ManyToMany
    @JoinTable(name = "user_x_role",
	    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
	    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user")
    private Collection<UserProjectAssignment> projectAssignments;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getDisplayName() {
	StringBuilder sb = new StringBuilder();
	sb.append(StringUtility.emptyIfNull(getFirstName()));
	if (sb.length() > 0) {
	    sb.append(" ");
	}
	sb.append(StringUtility.emptyIfNull(getLastName()));
	if (sb.length() == 0) {
	    sb.append(getUsername());
	}
	return sb.toString();
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public int getFailedLoginAttempts() {
	return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
	this.failedLoginAttempts = failedLoginAttempts;
    }

    public boolean isBlocked() {
	return blocked;
    }

    public void setBlocked(boolean blocked) {
	this.blocked = blocked;
    }

    public Date getLastLogin() {
	return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
	this.lastLogin = lastLogin;
    }

    public String getPasswordSalt() {
	return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
	this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
	return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
    }

    public Collection<Role> getRoles() {
	return roles;
    }

    public void setRoles(Collection<Role> roles) {
	this.roles = roles;
    }

    public Collection<UserProjectAssignment> getProjectAssignments() {
	return projectAssignments;
    }

    public void setProjectAssignments(Collection<UserProjectAssignment> projectAssignments) {
	this.projectAssignments = projectAssignments;
    }
}
