package org.leslie.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.leslie.server.mapping.MappedClass;
import org.leslie.server.mapping.MappedField;

@Entity
@Table(name = "role_x_permission")
@NamedQueries({
		@NamedQuery(name = RolePermission.QUERY_BY_ROLE_ID, query = ""
				+ "SELECT rp "
				+ "  FROM RolePermission rp "
				+ " WHERE rp.role.id = :roleId"),
		@NamedQuery(name = RolePermission.QUERY_BY_ROLE_ID_AND_PERMISSION_NAME, query = ""
				+ "SELECT rp "
				+ "  FROM RolePermission rp "
				+ " WHERE rp.role.id = :roleId "
				+ "   AND rp.permissionName = :permissionName "), })
@MappedClass
public class RolePermission {

	public static final String QUERY_BY_ROLE_ID = "RolePermission.byRoleId";
	public static final String QUERY_BY_ROLE_ID_AND_PERMISSION_NAME = "RolePermission.byRoleIdAndPermissionName";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@MappedField(readOnly = true, ignorePageData = true)
	private long id;

	@ManyToOne
	private Role role;

	@Column(name = "permission_name")
	@MappedField(pageFieldName = "name")
	private String permissionName;

	@Column(name = "level_uid")
	@MappedField(pageFieldName = "level")
	private int levelUid;

	public long getId() {
		return id;
	}

	void setId(long id) {
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
