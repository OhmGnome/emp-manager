package com.svm.hackathon.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svm.hackathon.datalayer.OfficeDAO;
import com.svm.hackathon.framework.GenericController;
import com.svm.hackathon.model.Office;

/**
 * 
 * A rest controller for CRUD operations involving the Office objects on the database.
 * 
 * @extends GenericController with an input of Office into the Generic Class.
 *
 */
@RestController
@RequestMapping("office")
public class OfficeController extends GenericController<Office>{
	
	@Autowired
	OfficeDAO officeDAO;
	
	@PostConstruct
	public void intit(){
		super.setDAO(officeDAO);
	}
	
}
