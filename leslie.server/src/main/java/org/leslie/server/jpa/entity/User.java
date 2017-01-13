package org.leslie.server.jpa.entity;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.leslie.server.jpa.mapping.ClassDataMapping;
import org.leslie.server.jpa.mapping.FieldDataMapping;
import org.leslie.server.jpa.mapping.impl.UserMapping;

@ClassDataMapping(UserMapping.class)
@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(name = User.QUERY_ALL, query = "SELECT u FROM User u "),
	@NamedQuery(name = User.QUERY_BY_USERNAME, query = "SELECT u FROM User u WHERE u.username = :username "),
	@NamedQuery(name = User.QUERY_BY_ROLE, query = "SELECT u FROM User u JOIN u.roles r WHERE r = :role "),
})
public class User {

    public static final String QUERY_ALL = "User.all";
    public static final String QUERY_BY_USERNAME = "User.byUsername";
    public static final String QUERY_BY_ROLE = "User.byRole";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @FieldDataMapping(formFieldName = "userId", readOnly = true)
    private long id;

    @FieldDataMapping
    private String username;

    @Column(name = "first_name")
    @FieldDataMapping
    private String firstName;

    @Column(name = "last_name")
    @FieldDataMapping
    private String lastName;

    @FieldDataMapping
    private String email;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;

    @FieldDataMapping
    private boolean blocked;

    @Column(name = "last_login")
    @FieldDataMapping(ignoreFormData = true)
    private Date lastLogin;

    @Column(name = "pw_salt")
    private String passwordSalt;

    @Column(name = "pw_hash")
    private String passwordHash;

    @ManyToMany
    @JoinTable(name = "user_x_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

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
}
