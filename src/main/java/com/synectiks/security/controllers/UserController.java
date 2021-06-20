/**
 * 
 */
package com.synectiks.security.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.commons.constants.IConsts;
import com.synectiks.commons.constants.IDBConsts;
import com.synectiks.commons.interfaces.IApiController;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.security.config.Constants;
import com.synectiks.security.entities.Organization;
import com.synectiks.security.entities.Status;
import com.synectiks.security.entities.User;
import com.synectiks.security.repositories.OrganizationRepository;
import com.synectiks.security.repositories.UserRepository;

/**
 * @author Rajesh
 */
@RestController
@RequestMapping(path = IApiController.SEC_API
		+ IApiController.URL_USER, method = RequestMethod.POST)
@CrossOrigin
public class UserController implements IApiController {

	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	private DefaultPasswordService pswdService = new DefaultPasswordService();

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	@RequestMapping(path = IConsts.API_FIND_ALL, method = RequestMethod.GET)
	//@RequiresRoles("ROLE_" + IConsts.ADMIN)
	public ResponseEntity<Object> findAll(HttpServletRequest request) {
		List<User> entities = null;
		try {
			entities = (List<User>) userRepository.findAll();
		} catch (Throwable th) {
			th.printStackTrace();
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(entities);
	}

	@Override
	@RequestMapping(IConsts.API_CREATE)
	public ResponseEntity<Object> create(@RequestBody ObjectNode service,
			HttpServletRequest request) {
		User user = new User();
		// check for duplicate username
		user.setUsername(service.get("username").asText());
		Optional<User> oUser = this.userRepository.findOne(Example.of(user));
		if(oUser.isPresent()) {
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Login id already exists");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		// check for duplicate email
		user = new User();
		user.setEmail(service.get("email").asText());
		oUser = this.userRepository.findOne(Example.of(user));
		if(oUser.isPresent()) {
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Email already exists");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		user = new User();
		try {
			String signedInUser = IUtils.getUserFromRequest(request);
//			user = IUtils.createEntity(service, signedInUser, User.class);
			createUserFromJson(user, service);
			// Encrypt the password
			if (!IUtils.isNullOrEmpty(user.getPassword()) &&
					!user.getPassword().startsWith("$shiro1$")) {
				user.setPassword(pswdService.encryptPassword(user.getPassword()));
			}
			Date currentDate = new Date();
			Organization organization = new Organization();
			organization.setName(service.get("organization").asText());
			Optional<Organization> oOrg = this.organizationRepository.findOne(Example.of(organization));
			if(oOrg.isPresent()) {
				user.setOrganization(oOrg.get());
			}else {
				logger.info("Saving new organization: "+ organization);
				organization.setCreatedAt(currentDate);
				organization.setUpdatedAt(currentDate);
				if(service.get("userName") != null) {
					organization.setCreatedBy(service.get("userName").asText());
					organization.setUpdatedBy(service.get("userName").asText());
		    	}else {
		    		organization.setCreatedBy(Constants.SYSTEM_ACCOUNT);
		    		organization.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		    	}
				organization = this.organizationRepository.save(organization);
				user.setOrganization(organization);
			}
			user.setCreatedAt(currentDate);
			user.setUpdatedAt(currentDate);
			
			if(service.get("userName") != null) {
				user.setCreatedBy(service.get("userName").asText());
				user.setUpdatedBy(service.get("userName").asText());
	    	}else {
	    		user.setCreatedBy(Constants.SYSTEM_ACCOUNT);
	    		user.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
	    	}
			
			logger.info("Saving user: " + user);
			user = userRepository.save(user);
		} catch (Throwable th) {
			th.printStackTrace();
			logger.error(th.getMessage(), th);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("User name already exists");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@Override
	@RequestMapping(IConsts.API_FIND_ID)
	public ResponseEntity<Object> findById(@PathVariable("id") String id) {
		User entity = null;
		try {
			entity = userRepository.findById(Long.parseLong(id)).orElse(null);
		} catch (Throwable th) {
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(entity);
	}

	@Override
	@RequestMapping(IConsts.API_DELETE_ID)
	public ResponseEntity<Object> deleteById(@PathVariable("id") String id) {
		try {
			userRepository.deleteById(id);
		} catch (Throwable th) {
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body("User removed Successfully");
	}

	@Override
	@RequestMapping(IConsts.API_UPDATE)
	public ResponseEntity<Object> update(@RequestBody ObjectNode reqObje,
			HttpServletRequest request) {
		User user = new User();
		// check for duplicate username
		user.setUsername(reqObje.get("username").asText());
		Optional<User> oUser = this.userRepository.findOne(Example.of(user));
		if(oUser.isPresent()) {
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Login id already exists");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		// check for duplicate email
		user = new User();
		user.setEmail(reqObje.get("email").asText());
		oUser = this.userRepository.findOne(Example.of(user));
		if(oUser.isPresent()) {
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Email already exists");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		try {
			logger.info("json: " + reqObje);
//			String signedInUser = IUtils.getUserFromRequest(request);
//			service = IUtils.createEntity(entity, user, User.class);
			user.setUsername(reqObje.get("username").asText());
			oUser = this.userRepository.findOne(Example.of(user));
			if(oUser.isPresent()) {
				createUserFromJson(user, reqObje);
				Date currentDate = new Date();
				
				if(user.getOrganization() == null) {
					logger.info("Saving new organization: "+ reqObje.get("organization").asText());
					Organization organization = new Organization();
					organization.setCreatedAt(currentDate);
					organization.setUpdatedAt(currentDate);
					if(reqObje.get("userName") != null) {
						organization.setCreatedBy(reqObje.get("userName").asText());
						organization.setUpdatedBy(reqObje.get("userName").asText());
			    	}else {
			    		organization.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			    		organization.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			    	}
					organization = this.organizationRepository.save(organization);
					user.setOrganization(organization);
				}
				
				// Encrypt the password
				if (!IUtils.isNullOrEmpty(user.getPassword()) &&
						!user.getPassword().startsWith("$shiro1$")) {
					user.setPassword(pswdService.encryptPassword(user.getPassword()));
				}
				user.setUpdatedAt(currentDate);
				
				if(reqObje.get("userName") != null) {
					user.setUpdatedBy(reqObje.get("userName").asText());
		    	}else {
		    		user.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		    	}
				logger.info("Updating user: " + user);
				userRepository.save(user);
			}else {
				logger.warn("user not found.");
			}
			
		} catch (Throwable th) {
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@Override
	@RequestMapping(IConsts.API_DELETE)
	public ResponseEntity<Object> delete(@RequestBody ObjectNode entity) {
		if (!IUtils.isNull(entity.get(IDBConsts.Col_ID))) {
			return deleteById(entity.get(IDBConsts.Col_ID).asText());
		}
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
				.body("Not a valid entity");
	}
	
	
	@RequestMapping(method = RequestMethod.GET, path = IApiController.URL_SEARCH)
	public ResponseEntity<Object> search(@RequestBody ObjectNode reqObj) {
		boolean isFilter = false;
		User user = null;
		List<User> list = null;
		try {
			if (reqObj.get("id") != null) {
				user.setId(Long.parseLong(reqObj.get("id").asText()));
				isFilter = true;
			}
			if (reqObj.get("username") != null) {
				user.setUsername(reqObj.get("username").asText());
				isFilter = true;
			}
			if (reqObj.get("email") != null) {
				user.setEmail(reqObj.get("email").asText());
				isFilter = true;
			}
			if (reqObj.get("type") != null) {
				user.setType(reqObj.get("type").asText());
				isFilter = true;
			}
			if (reqObj.get("organization") != null) {
				Organization organization = new Organization();
				organization.setName(reqObj.get("organization").asText());
				Optional<Organization> oOrg = this.organizationRepository.findOne(Example.of(organization));
				if(oOrg.isPresent()) {
					user.setOrganization(oOrg.get());
				}
				isFilter = true;
			}
			
			if (reqObj.get("active") != null) {
				user.setActive(reqObj.get("active").asBoolean());
				isFilter = true;
			}
			if (reqObj.get("ownerId") != null) {
				user.setOwnerId(reqObj.get("active").asLong());
				isFilter = true;
			}
			
			if (isFilter) {
				list = this.userRepository.findAll(Example.of(user), Sort.by(Direction.DESC, "id"));
			} else {
				list = this.userRepository.findAll(Sort.by(Direction.DESC, "id"));
			}
		} catch (Throwable th) {
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	
	private void createUserFromJson(User user, ObjectNode reqObj) {
		if(reqObj.get("type") != null ) {
			user.setType(reqObj.get("type").asText());
		}
		if(reqObj.get("username") != null ) {
			user.setUsername(reqObj.get("username").asText());
		}
		if(reqObj.get("password") != null ) {
			user.setPassword(reqObj.get("password").asText());
		}
		if(reqObj.get("active") != null) {
			user.setActive(reqObj.get("active").asBoolean());
		}
		if(reqObj.get("email") != null) {
			user.setEmail(reqObj.get("email").asText());
		}
		
		user.setOwnerId(reqObj.get("ownerId") != null ? reqObj.get("ownerId").asLong() : null);
		if(reqObj.get("organization") != null) {
			Organization organization = new Organization();
			organization.setName(reqObj.get("organization").asText());
			Optional<Organization> oOrg = this.organizationRepository.findOne(Example.of(organization));
			if(oOrg.isPresent()) {
				user.setOrganization(oOrg.get());
			}
		}
	}
	
}
