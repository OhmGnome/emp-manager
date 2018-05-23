package com.svm.hackathon.datalayer;

import javax.annotation.PostConstruct;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svm.hackathon.framework.GenericDAO;
import com.svm.hackathon.model.Authentication;
import com.svm.hackathon.model.User;

@Repository
@Transactional
public class UserDAO extends GenericDAO<User>{

	@PostConstruct
	public void init(){
		super.setClass(User.class);
	}
	
	public User getByUsername(String username){
		Query query = super.getEM().createQuery("from User where username = :username");
		query.setParameter("username", username);
		return (User) query.getSingleResult();
	}

}
