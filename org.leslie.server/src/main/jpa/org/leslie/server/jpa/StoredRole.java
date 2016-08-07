package org.leslie.server.jpa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class StoredRole {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "role_id")
	private List<StoredRolePermission> rolePermissions;

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

	public List<StoredRolePermission> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(List<StoredRolePermission> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

}
