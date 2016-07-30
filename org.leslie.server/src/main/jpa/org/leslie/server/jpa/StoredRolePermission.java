package org.leslie.server.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import leslie.org.leslie.shared.security.PermissionLevel;

@Entity
@Table(name = "role_x_permission")
public class StoredRolePermission {

	@Id
	@Column(name = "id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private StoredRole role;

	@Column(name = "permission_class_name")
	private String permissionClassName;

	@Column(name = "level_uid")
	private int levelUid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StoredRole getRole() {
		return role;
	}

	public void setRole(StoredRole role) {
		this.role = role;
	}

	public String getPermissionClassName() {
		return permissionClassName;
	}

	public void setPermissionClassName(String permissionClassName) {
		this.permissionClassName = permissionClassName;
	}

	public int getLevelUid() {
		return levelUid;
	}

	public void setLevelUid(int levelUid) {
		this.levelUid = levelUid;
	}

	public PermissionLevel getLevel() {
		return PermissionLevel.getInstance(levelUid);
	}

	public void setLevel(PermissionLevel level) {
		this.levelUid = level.getValue();
	}
}
