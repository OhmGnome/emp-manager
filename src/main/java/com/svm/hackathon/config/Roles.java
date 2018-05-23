package com.svm.hackathon.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * HardCoded user roles. This is better implemented by overriding the configureGlobal method of the
 * WebSecurityConfigurerAdapter interface but I want a custom REST role getter for the frontend. This approach should be
 * coupled with spring security to protect the backend because of the inherent security holes in the frontend, where the
 * end user has access to the code.
 * 
 */
public class Roles {
	public static final String EDITOR = "EDITOR";
	public static final String USER = "USER";
	
	private HashMap<String, List<String>> roles = new HashMap<String, List<String>>();
	
	private static Roles instance = new Roles();

	public Roles() {
		//TODO remove for release
		this.roles.put("fake@fakeMail.com", Arrays.asList(USER, EDITOR));
	}

	public HashMap<String, List<String>> getRoles() {
		return roles;
	}

	public static Roles getInstance() {
		return instance;
	}

}
