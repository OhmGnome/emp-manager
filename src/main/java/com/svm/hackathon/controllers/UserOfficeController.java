package com.svm.hackathon.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.svm.hackathon.datalayer.UserOfficeDAO;
import com.svm.hackathon.model.Office;
import com.svm.hackathon.model.User;

/**
 * 
 * 
 * 
 * A rest controller for CRUD operations involving the many to many mapping between offices and users.
 *
 */
@RestController
public class UserOfficeController {

	@Autowired
	UserOfficeDAO userOfficeDAO;
	
	/**
	 * Adds offices to an individual user.
	 * 
	 * The userId parameter on the input body should contain a single integer with the userId.
	 * The officeIds parameter should contain a list of officeIds to add to the user.
	 * 
	 * Initial functionality only allows for one user to be added to one office, but an office can hold many users.
	 * 
	 * @param officeMap A map with two fields: userId and officeIds.
	 * @return The list of offices that have been mapped to the given user.
	 */
	@RequestMapping(value = "/addOfficesToUser", method = RequestMethod.POST)
	public ResponseEntity<List<Office>> addSkillToUser(@RequestBody Map<String, Object> officeMap){
		try {
			return new ResponseEntity<List<Office>>(userOfficeDAO.addOfficesToUser((Integer)officeMap.get("userId"), (List<Integer>)officeMap.get("officeIds")), HttpStatus.OK);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new ResponseEntity<List<Office>>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
			return new ResponseEntity<List<Office>>(HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
	
	/**
	 * Gets a list of users for a given office Id.
	 * 
	 * @param officeId
	 * @return A list of users working at the input office.
	 */
	@RequestMapping(value = "getUserByOffice/{officeId}", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getUserBySkill(@PathVariable Integer officeId){
		return new ResponseEntity<List<User>>(userOfficeDAO.getUsersByOffice(officeId), HttpStatus.OK);
	}
	
	/**
	 * Gets a list of offices based on the input user.
	 * 
	 * For now should only return a single office, but multiple mappings may be added in later.
	 * 
	 * There is currently no check for multiple office added to one user, except in the mapping function.
	 * 
	 * @param userId
	 * @return The list of offices associated with the given user.
	 */
	@RequestMapping(value = "getOfficeByUser/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Office>> getSkillByUser(@PathVariable Integer userId){
		return new ResponseEntity<List<Office>>(userOfficeDAO.getOfficesByUser(userId), HttpStatus.OK);
	}
	
	/**
	 * Deletes a user-office mapping from the database.
	 * 
	 * @param mappedOffice
	 * @return True if the mapping was successfully deleted.
	 */
	@RequestMapping(value = "deleteUserOfficeMapping", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteUserOfficeMapping(@RequestBody Map<String, Integer> mappedOffice){
		return new ResponseEntity<Boolean>(userOfficeDAO.deleteUserOfficeMapping((Integer)mappedOffice.get("userId"), (Integer)mappedOffice.get("officeId")), HttpStatus.OK);
	}
}
