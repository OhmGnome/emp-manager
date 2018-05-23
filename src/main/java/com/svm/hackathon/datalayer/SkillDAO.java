package com.svm.hackathon.datalayer;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svm.hackathon.framework.GenericDAO;
import com.svm.hackathon.model.Office;
import com.svm.hackathon.model.Skill;

@Repository
@Transactional
public class SkillDAO extends GenericDAO<Skill>{

	@PostConstruct
	public void init(){
		super.setClass(Skill.class);
	}
	
}
