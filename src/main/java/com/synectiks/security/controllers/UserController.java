/**
 * 
 */
package com.synectiks.security.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.commons.constants.IConsts;
import com.synectiks.commons.constants.IDBConsts;
import com.synectiks.commons.interfaces.IApiController;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.security.entities.User;
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
	private UserRepository repository;

	@Override
	@RequestMapping(path = IConsts.API_FIND_ALL, method = RequestMethod.GET)
	//@RequiresRoles("ROLE_" + IConsts.ADMIN)
	public ResponseEntity<Object> findAll(HttpServletRequest request) {
		List<User> entities = null;
		try {
			entities = (List<User>) repository.findAll();
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
		User entity = null;
		try {
			String user = IUtils.getUserFromRequest(request);
			entity = IUtils.createEntity(service, user, User.class);
			// Encrypt the password
			if (!IUtils.isNullOrEmpty(entity.getPassword()) &&
					!entity.getPassword().startsWith("$shiro1$")) {
				entity.setPassword(pswdService.encryptPassword(entity.getPassword()));
			}
			logger.info("Saving user: " + entity);
			entity = repository.save(entity);
		} catch (Throwable th) {
			th.printStackTrace();
			//logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(entity);
	}

	@Override
	@RequestMapping(IConsts.API_FIND_ID)
	public ResponseEntity<Object> findById(@PathVariable("id") String id) {
		User entity = null;
		try {
			entity = repository.findById(id).orElse(null);
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
			repository.deleteById(id);
		} catch (Throwable th) {
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body("User removed Successfully");
	}

	@Override
	@RequestMapping(IConsts.API_UPDATE)
	public ResponseEntity<Object> update(@RequestBody ObjectNode entity,
			HttpServletRequest request) {
		User service = null;
		try {
			logger.info("json: " + entity);
			String user = IUtils.getUserFromRequest(request);
			service = IUtils.createEntity(entity, user, User.class);
			// Encrypt the password
			if (!IUtils.isNullOrEmpty(service.getPassword()) &&
					!service.getPassword().startsWith("$shiro1$")) {
				service.setPassword(pswdService.encryptPassword(service.getPassword()));
			}
			logger.info("Updating user: " + service);
			repository.save(service);
		} catch (Throwable th) {
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
		}
		return ResponseEntity.status(HttpStatus.OK).body(service);
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
}
