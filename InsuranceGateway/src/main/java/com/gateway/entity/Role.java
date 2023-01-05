package com.gateway.entity;

import javax.persistence.*;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long roleId;

	@Column(nullable = false, unique = true)
	private String roleName;

	@Override
	public String toString() {
		return "Role{" +
				"roleName='" + roleName + '\'' +
				'}';
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public Role() {
		super();
	}

}
