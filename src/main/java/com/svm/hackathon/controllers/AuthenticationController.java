package com.svm.hackathon.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.svm.hackathon.datalayer.AuthenticationDAO;
import com.svm.hackathon.model.Authentication;
import com.svm.hackathon.model.User;
import com.svm.hackathon.util.JwtUtil;

/**
 * 
 *         A Rest Controller for authentication operations.
 * 
 *         Authentication includes logging in, logging out, and creation of a new user.
 * 
 *         Other authentication changes will be handled through custom endpoints on the user controller.
 *
 */
@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationDAO authDAO;

	/**
	 * Creates a new user for initial registration.
	 * 
	 * @param newUserAuth
	 *            an authentication object containing email, username, and password.
	 * @return The new User object from the registration.
	 */
	@RequestMapping(value = "newUser", method = RequestMethod.POST)
	public ResponseEntity<User> newUser(@RequestBody Authentication newUserAuth) {
		return new ResponseEntity<User>(authDAO.newUser(newUserAuth), HttpStatus.OK);
	}

	/**
	 * An authentication end point for loggin in a new user.
	 * 
	 * This end point handles session creation.
	 * 
	 * @param userAuth
	 *            an authentication object that has the username and password of the user that intends to log in.
	 * @param request
	 *            The request object on which the session will be manipulated.
	 * @return A boolean of true if the user was successfully logged in.
	 */
	@RequestMapping(value = "authenticateUser", method = RequestMethod.POST)
	public ResponseEntity<LoginResponse> authenticate(@RequestBody Authentication userAuth, HttpServletRequest request) {
		boolean authSuccess = authDAO.authenticateUser(userAuth);
		LoginResponse loginResponse = new LoginResponse();
		String username = userAuth.getUsername();
		String email = authDAO.getAuthenticatedEmail(userAuth);
		
		if (authSuccess) {
			request.getSession().setAttribute("loggedin", "true");
			loginResponse.token = JwtUtil.tokenGenerator(username, email);
			loginResponse.isAuthorized =  true;
			return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
		} else {
			loginResponse.token = null;
			loginResponse.isAuthorized =  true;
			return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.FORBIDDEN);
		}
	}
	
	private class LoginResponse{
		public String token;
		public boolean isAuthorized = false;
		
		public LoginResponse(){}
	}

	/**
	 * The logout end point that will destroy the session whenever the endpoint is hit.
	 * 
	 * @param request
	 *            the Http Request with the session attached.
	 * @return A boolean of true if the session was destroyed.
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ResponseEntity<Boolean> logout(HttpServletRequest request) {
		request.getSession().invalidate();
		if (request.getSession().getAttribute("loggedin") != null) {
			if (request.getSession().getAttribute("loggedin").equals("true")) {
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "getSessionAttributeLoggedin", method = RequestMethod.GET)
	public ResponseEntity<Boolean> getSession(HttpServletRequest request) {
		if (request.getSession().getAttribute("loggedin") != null) {
			if (request.getSession().getAttribute("loggedin").equals("true")) {
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
	}

}
