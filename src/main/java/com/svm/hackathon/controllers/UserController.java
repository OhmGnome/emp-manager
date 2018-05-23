package com.svm.hackathon.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.svm.hackathon.config.Roles;
import com.svm.hackathon.datalayer.UserDAO;
import com.svm.hackathon.framework.GenericController;
import com.svm.hackathon.model.User;
import com.svm.hackathon.services.TwilioSMS;
import com.svm.hackathon.util.JwtUtil;
import com.twilio.sdk.TwilioRestException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 
 * 
 * 
 *         A rest controller for CRUD operations for the User object.
 *
 */
@RestController
@RequestMapping("user")
public class UserController extends GenericController<User> {

	@Autowired
	UserDAO userDAO;

	@PostConstruct
	public void intit() {
		super.setDAO(userDAO);
	}

	/**
	 * Gets an individual user by the username field.
	 * 
	 * @param username
	 * @return A response entity with the user object in the response body.
	 */
	@RequestMapping(value = "/name/{username}", method = RequestMethod.GET)
	ResponseEntity<User> getByUsername(@PathVariable String username) {
		return new ResponseEntity<User>(userDAO.getByUsername(username), HttpStatus.OK);
	}

	@RequestMapping(value = "/role/{username}", method = RequestMethod.GET)
	ResponseEntity<HashMap<String, List<String>>> getRoleByUsername(@PathVariable String username, HttpServletRequest request)
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
			IllegalArgumentException, UnsupportedEncodingException {
		User user = userDAO.getByUsername(username);
		HashMap<String, List<String>> roles = new HashMap<String, List<String>>();
		Iterator iterator = Roles.getInstance().getRoles().entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, List<String>> entry = (Entry<String, List<String>>) iterator.next();
			if (entry.getKey().equals(user.getEmail())) {
				String token = request.getHeader("Authorization");
				roles.put("roles", JwtUtil.verifyRoles(token, entry.getValue()));
				break;
			}
		}
		return new ResponseEntity<HashMap<String,List<String>>>(roles,HttpStatus.OK);
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	ResponseEntity<User> send(@RequestBody String[] data) throws TwilioRestException, IOException {
		User user = new User();
		if ((data[0] != null || data[1] != null) && data[2] == null) {
			TwilioSMS.sendSMS(data[0], data[1], null);
		} else if (data[0] != null && data[1] != null && data[2] != null) {
			TwilioSMS.sendSMS(data[0], data[1], data[2]);
		} else {
			return new ResponseEntity<User>(user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	ResponseEntity<String> verify(@RequestBody String phonenum) throws TwilioRestException {
		String validationCode = TwilioSMS.verifyNumber(phonenum);
		return new ResponseEntity<String>(validationCode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/isItVerified", method = RequestMethod.POST)
	ResponseEntity<Boolean> isVerified(@RequestBody String phonenum) throws TwilioRestException{
		boolean isVerified = TwilioSMS.isVerified(phonenum);
		return new ResponseEntity<Boolean>(isVerified, HttpStatus.OK);
	}
}
