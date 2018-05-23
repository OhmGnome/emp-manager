package com.svm.hackathon.datalayer;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svm.hackathon.model.Authentication;
import com.svm.hackathon.model.User;
import com.svm.hackathon.services.EmailService;

@Repository
@Transactional
public class AuthenticationDAO {

	@Autowired
	EntityManager entityManager;
	
	@Autowired
	UserDAO userDAO;
	
	public User newUser(Authentication userAuth){
		UUID token = UUID.randomUUID();
		User user = new User();
		userAuth.setPassword(BCrypt.hashpw(userAuth.getPassword(), BCrypt.gensalt()));
		userAuth.setToken(token.toString());
		user.setEmail(userAuth.getEmail());
		user.setUsername(userAuth.getUsername());
		entityManager.persist(user);
		entityManager.persist(userAuth);
		entityManager.merge(user);
		EmailService.sendVerificationEmail(userAuth.getEmail());
		return user;
	}
	
	public Boolean authenticateUser(Authentication authentication){
		Query query = entityManager.createQuery("from Authentication where username = :username").setParameter("username", authentication.getUsername());
//		if(query.getResultList().size() != 1){
//			throw new Exception("The user with that username was not found.");
//		}
		Authentication userAuth = (Authentication) query.getSingleResult();
		boolean authenticated = BCrypt.checkpw(authentication.getPassword(), userAuth.getPassword());
		return authenticated;
	}
	
	public String getAuthenticatedEmail(Authentication authentication){
		Query query = entityManager.createQuery("from Authentication where username = :username").setParameter("username", authentication.getUsername());
//		if(query.getResultList().size() != 1){
//			throw new Error("The user with that username was not found.");
//		}
		Authentication userAuth = (Authentication) query.getSingleResult();
		return userAuth.getEmail();
	}
}
