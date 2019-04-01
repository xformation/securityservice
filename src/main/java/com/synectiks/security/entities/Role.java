package com.synectiks.security.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.synectiks.commons.constants.IConsts;
import com.synectiks.commons.constants.IDBConsts;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
@Entity
@Table(name = IDBConsts.Tbl_ROLES)
public class Role extends PSqlEntity {

	private static final long serialVersionUID = 2619620405443093727L;
	public static final Role ROLE_ADMIN = create(IConsts.ADMIN);

	private String name;
	private Long version;
	private boolean group;
	private String description;
	@OneToMany(targetEntity = Permission.class, fetch = FetchType.EAGER)
	private List<Permission> permissions;
	@OneToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
	private Set<Role> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Method to check if role is a group of roles
	 * @return
	 */
	public boolean isGroup() {
		return this.group;
	}

	public void setGroup(boolean grp) {
		this.group = grp;
	}

	@Override
	public String toString() {
		return "{" + (version != null ? "\"version\": \"" + version + "\", " : "")
				+ (name != null ? "\"name\": \"" + name + "\", " : "")
				+ (description != null ? "\"description\": \"" + description + "\", " : "")
				+ (permissions != null ? "\"permissions\": " + permissions + ", " : "")
				+ (roles != null ? "\"roles\": " + roles + ", " : "")
				+ (id > 0 ? "\"id\": " + id + ", " : "")
				+ ("\"group\": " + group + ", ")
				+ (createdAt != null ? "\"createdAt\": \"" + createdAt + "\", " : "")
				+ (updatedAt != null ? "\"updatedAt\": \"" + updatedAt + "\", " : "")
				+ (createdBy != null ? "\"createdBy\": \"" + createdBy + "\", " : "")
				+ (updatedBy != null ? "\"updatedBy\": \"" + updatedBy + "\"" : "")
				+ "}";
	}

	private static Role create(String roleName) {
		Role role = new Role();
		role.setName("ROLE_" + roleName);
		return role;
	}

}
