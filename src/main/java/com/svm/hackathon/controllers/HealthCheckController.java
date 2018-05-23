package com.svm.hackathon.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

	/**
	 * REST service for AWS health check. important
	 */
@RestController
public class HealthCheckController {

	@RequestMapping(value = "/health", method = RequestMethod.GET)
	ResponseEntity health() {
		return new ResponseEntity(HttpStatus.OK);
	}
	
	//not required
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	ResponseEntity<String> ping() {
		return new ResponseEntity<String>("ping reply", HttpStatus.OK);
	}
	
}
