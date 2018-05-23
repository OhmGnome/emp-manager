package com.svm.hackathon.controllers;

import java.util.HashMap;
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

import com.svm.hackathon.datalayer.UserSkillDAO;
import com.svm.hackathon.model.Skill;
import com.svm.hackathon.model.User;

@RestController
public class UserSkillController {

	@Autowired
	UserSkillDAO userSkillDAO;
	
	@RequestMapping(value = "/addSkillsToUser", method = RequestMethod.POST)
	public ResponseEntity<List<Skill>> addSkillToUser(@RequestBody Map<String, Object> skillMap){
		try {
			return new ResponseEntity<List<Skill>>(userSkillDAO.addSkillsToUser((Integer)skillMap.get("userId"), (List<Integer>)skillMap.get("skillIds")), HttpStatus.OK);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new ResponseEntity<List<Skill>>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
			return new ResponseEntity<List<Skill>>(HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
	
	@RequestMapping(value = "/getOrderedUserSkills/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getOrderedSkills(@PathVariable Integer userId){
		return new ResponseEntity<List<Map<String,Object>>>(userSkillDAO.getSkillsByUserSkillId(userId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "getUserBySkill/{skillId}", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getUserBySkill(@PathVariable Integer skillId){
		return new ResponseEntity<List<User>>(userSkillDAO.getUserBySkill(skillId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "getSkillByUser/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Skill>> getSkillByUser(@PathVariable Integer userId){
		return new ResponseEntity<List<Skill>>(userSkillDAO.getSkillByUser(userId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "deleteUserSkill", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteUserSkill(@RequestBody Map<String, Integer> skillMap){
		return new ResponseEntity<Boolean>(userSkillDAO.unmapUserSkill((Integer)skillMap.get("userId"), (Integer)skillMap.get("skillId")), HttpStatus.OK);
	}
}
