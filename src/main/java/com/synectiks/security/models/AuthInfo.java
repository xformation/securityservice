/**
 * 
 */
package com.synectiks.security.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationInfo;

import com.synectiks.commons.utils.IUtils;
import com.synectiks.security.entities.Permission;
import com.synectiks.security.entities.Role;
import com.synectiks.security.entities.User;

/**
 * @author Rajesh
 */
@SuppressWarnings("unused")
public class AuthInfo implements Serializable {

	private static final long serialVersionUID = -4160134298113121592L;

	private Info info;
	private Authz authz;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Authz getAuthz() {
		return authz;
	}

	public void setAuthz(Authz authz) {
		this.authz = authz;
	}

	private static class Principal {

		private String login;
		private String apiKey;

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}
	}

	private static class Credential implements Serializable {

		private static final long serialVersionUID = 9037797296682387603L;

		private String name;
		private String email;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

	private static class Info implements Serializable {

		private static final long serialVersionUID = 5654480041306718760L;

		private Principal principal;
		private Credential credentials;

		public Object getPrincipal() {
			return principal;
		}

		public void setPrincipal(Principal principal) {
			this.principal = principal;
		}

		public Credential getCredentials() {
			return credentials;
		}

		public void setCredentials(Credential credentials) {
			this.credentials = credentials;
		}
	}

	private static class Authz implements Serializable {

		private static final long serialVersionUID = 1206087200585365971L;

		private List<String> roles;
		private List<String> permissions;
		private Map<String, List<String>> mapPermissions;
		
		
		public Map<String, List<String>> getMapPermissions() {
			return mapPermissions;
		}

		public void setMapPermissions(Map<String, List<String>> mapPermissions) {
			this.mapPermissions = mapPermissions;
		}

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

		public List<String> getPermissions() {
			return permissions;
		}

		public void setPermissions(List<String> permissions) {
			this.permissions = permissions;
		}

	}

	public static AuthInfo create(AuthenticationInfo info, User usr) {
		if (!IUtils.isNull(info) && !IUtils.isNull(usr)) {
			//Principal p = new Principal();
			//p.setLogin(info.getPrincipals().);
			AuthInfo ai = new AuthInfo();
			Info i = new Info();
			Principal p = new Principal();
			p.setLogin(info.getPrincipals().getPrimaryPrincipal().toString());
			p.setApiKey(usr.getPassword());
			i.setPrincipal(p);
			Credential cred = new Credential();
			cred.setEmail(usr.getEmail());
			cred.setName(usr.getUsername());
			i.setCredentials(cred);
			ai.setInfo(i);
			List<String> rls = new ArrayList<>();
			List<String> perms = new ArrayList<>();
			Map<String, List<String>> mapPermissions = new HashMap<String, List<String>>();
			fillRolePerms(rls, perms, usr.getRoles(),mapPermissions);
			Authz authz = new Authz();
			authz.setRoles(rls);
			authz.setPermissions(perms);
			authz.setMapPermissions(mapPermissions);
			ai.setAuthz(authz);
			return ai;
		}
		return null;
	}

	private static void fillRolePerms(List<String> rls, List<String> perms,
			Collection<Role> roles,Map<String,List<String>> mapPermission) {
		for (Role r : roles) {
			rls.add(r.getName());
			if (r.isGrp()) {
				fillRolePerms(rls, perms, r.getRoles(),mapPermission);
			} else {
				for (Permission p : r.getPermissions()) {
					List<String> lst=new ArrayList<String>();
					if(mapPermission.containsKey(p.getName())) {
						lst.addAll(mapPermission.get(p.getName()));
						lst.add(p.getPermission());
						mapPermission.replace(p.getName(), lst);
					}else {
						lst.add(p.getPermission());
						mapPermission.put(p.getName(), lst);
					}
					perms.add(p.getPermission());
				}
	
			}
		}
	}
}
