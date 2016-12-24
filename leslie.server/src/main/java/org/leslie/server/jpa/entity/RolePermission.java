package org.leslie.server.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_x_permission")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Role role;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "level_uid")
    private int levelUid;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public String getPermissionName() {
	return permissionName;
    }

    public void setPermissionName(String permissionName) {
	this.permissionName = permissionName;
    }

    public int getLevelUid() {
	return levelUid;
    }

    public void setLevelUid(int levelUid) {
	this.levelUid = levelUid;
    }
}
