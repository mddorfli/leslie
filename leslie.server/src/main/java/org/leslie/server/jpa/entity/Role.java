package org.leslie.server.jpa.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({ @NamedQuery(name = Role.QUERY_ALL, query = "SELECT r FROM Role r"),
		@NamedQuery(name = Role.QUERY_BY_IDS, query = "SELECT r FROM Role r WHERE r.id IN :roleIds") })
public class Role {

	public static final String QUERY_ALL = "Role.all";
	public static final String QUERY_BY_IDS = "Role.byIds";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@OneToMany
	@JoinColumn(name = "role_id")
	private Collection<RolePermission> rolePermissions;

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

	public Collection<RolePermission> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(Collection<RolePermission> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

}
