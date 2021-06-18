/**
 * 
 */
package com.synectiks.security.entities;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.synectiks.commons.constants.IConsts;
import com.synectiks.commons.constants.IDBConsts;
import com.synectiks.commons.entities.PSqlEntity;
import com.synectiks.security.config.Constants;

/**
 * @author Rajesh
 */
@Entity
@Table(name = IDBConsts.Tbl_USERS)
public class User extends PSqlEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static User ADMIN = create(IConsts.ADMIN, Arrays.asList(Role.ROLE_ADMIN));
	public static String ADMIN_UUID = "7afb71b8-2db0-4fc0-b66f-47f34d10a5a3";

//	public static enum Types {
//		CUSTOMER, USER, SUPER;
//	}

	@Column(nullable = true)
	private String type;
	private String username;
	private String password;
	private boolean active = true;
	private String email;
	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
	private List<Role> roles;
	
	@OneToOne(targetEntity = Organization.class, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = "organizationalUnit", allowSetters = true)
    private Organization organization;

	@Column(nullable = true)
	private Long ownerId;
	
	public User() {
		super();
	}

	public User(String username) {
		this();
		this.setUsername(username);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Method to create a user object with username and roles
	 * @param username
	 * @param asList
	 * @return
	 */
	private static User create(String username, List<Role> asList) {
		User user = new User();
		user.setUsername(username);
		user.setType(Constants.USER_TYPE_ADMIN);
		user.setRoles(asList);
		return user;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "User [type=" + type + ", username=" + username + ", password=" + password + ", active=" + active
				+ ", email=" + email + ", roles=" + roles + ", organization=" + organization + ", ownerId=" + ownerId
				+ "]";
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	
}
