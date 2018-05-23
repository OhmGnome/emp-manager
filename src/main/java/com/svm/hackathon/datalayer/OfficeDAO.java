package com.svm.hackathon.datalayer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svm.hackathon.framework.GenericDAO;
import com.svm.hackathon.model.Office;

@Repository
@Transactional
public class OfficeDAO extends GenericDAO<Office>{

	@PostConstruct
	public void init(){
		super.setClass(Office.class);
	}

}
