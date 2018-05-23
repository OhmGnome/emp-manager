package com.svm.hackathon.config;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.svm.hackathon.datalayer.AuthenticationDAO;
import com.svm.hackathon.datalayer.UserDAO;
import com.svm.hackathon.model.Authentication;
import com.svm.hackathon.model.User;

/**
 * 
 *         Pre register a user smd2 and password smd2 into the database so that developers can skip a few steps
 *         whenever they deploy to a new cloud TODO remove for release
 */
@Component
public class DevLogin {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AuthenticationDAO authDAO;
	@PersistenceContext
	private EntityManager entityManager;

	Timer timer;

	@PostConstruct
	public void init() {
		timer = new Timer();
		long boot = 360000;
		timer.schedule(new Timeslip(), boot);
	}

	private class Timeslip extends TimerTask {
		public void run() {

			User user = new User();
			user.setEmail("fake@fakeMail.com");
			user.setUsername("smd2");
			user.setAddress1("18850 N 56th St");
			user.setAddress2("Phoenix AZ");

			Authentication auth = new Authentication();
			auth.setEmail("fake@fakeMail.com");
			auth.setUsername("smd2");
			auth.setPassword("smd2");
			
			try {
				Object authExists = entityManager.createNativeQuery("SELECT * FROM authentication WHERE username = 'smd2' ORDER BY id DESC LIMIT 1").getSingleResult();
				if (authExists == null) {
					authDAO.newUser(auth);
				}
			} catch (NoResultException e) {
				e.printStackTrace();
				authDAO.newUser(auth);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Object userExists = entityManager.createNativeQuery("SELECT * FROM user WHERE username = 'smd2' ORDER BY id DESC LIMIT 1").getSingleResult();
					if (userExists == null) {
						userDAO.save(user);
					}
				} catch (NoResultException ex) {
					ex.printStackTrace();
					userDAO.save(user);
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
