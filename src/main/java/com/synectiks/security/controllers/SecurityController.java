/**
 * 
 */
package com.synectiks.security.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.commons.interfaces.IApiController;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.schemas.entities.User;
import com.synectiks.security.models.LoginRequest;

/**
 * @author Rajesh
 */
@RestController
@RequestMapping(path = IApiController.PUB_API, method = RequestMethod.POST)
public class SecurityController {

	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam  String username, @RequestParam String password,
			@RequestParam(required = false) boolean rememberMe,
			@RequestParam(defaultValue = "") String redirectTo) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(username);
		token.setPassword(password.toCharArray());
		token.setRememberMe(rememberMe);
		ResponseEntity<Object> res = authenticate(token);
		if (!IUtils.isNullOrEmpty(redirectTo)) {
			return redirectTo;
		}
		return res.getBody().toString();
	}

	@RequestMapping(value = "/signup")
	public String login(@RequestBody LoginRequest request) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(request.getUsername());
		token.setPassword(request.getPassword().toCharArray());
		token.setRememberMe(request.isRememberMe());
		ResponseEntity<Object> res = authenticate(token);
		if (!IUtils.isNullOrEmpty(request.getRedirectTo())) {
			return request.getRedirectTo();
		}
		return res.getBody().toString();
	}

	@RequestMapping(value = "/singin")
	public ResponseEntity<Object> login(@RequestBody User user) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(user.getUsername());
		token.setPassword(user.getPassword().toCharArray());
		return authenticate(token);
	}

	@RequestMapping(value = "/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody final UsernamePasswordToken token) {
        logger.info("Authenticating {}", token.getUsername());
        final Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            //if no exception, that's it, we're done!
        } catch (UnknownAccountException th) {
            //username wasn't in the system, show them an error message?
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
        } catch (IncorrectCredentialsException th) {
            //password didn't match, try again?
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
        } catch (LockedAccountException th) {
            //account for that username is locked - can't login.  Show them a message?
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
        } catch (AuthenticationException th) {
        	// General exception thrown due to an error during the Authentication process
			logger.error(th.getMessage(), th);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "Success";
	}

}
