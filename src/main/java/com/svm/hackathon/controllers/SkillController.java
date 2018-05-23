package com.svm.hackathon.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svm.hackathon.datalayer.SkillDAO;
import com.svm.hackathon.framework.GenericController;
import com.svm.hackathon.model.Skill;

/**
 * 
 * 
 * 
 * A Rest Controller for CRUD operations involving the Skill object.
 *
 */
@RestController
@RequestMapping("skill")
public class SkillController extends GenericController<Skill>{

	@Autowired
	SkillDAO skillDAO;
	
	@PostConstruct
	public void intit(){
		super.setDAO(skillDAO);
	}
	
}
