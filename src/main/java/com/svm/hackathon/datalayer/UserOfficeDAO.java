package com.svm.hackathon.datalayer;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svm.hackathon.framework.GenericManyToManyDAO;
import com.svm.hackathon.model.Office;
import com.svm.hackathon.model.User;
import com.svm.hackathon.model.UserOffice;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public class UserOfficeDAO extends GenericManyToManyDAO<UserOffice, User, Office> {
	
	@PostConstruct
	public void init(){
		super.setClasses(UserOffice.class, User.class, Office.class);
		super.setIdFields("UserOffice", "user", "office");
	}
	
	public List<Office> addOfficesToUser(Integer userId, List<Integer> officeIds) throws InstantiationException, IllegalAccessException{
		if(checkUserOfficeMapping(userId)){
			throw new Error("You cannot map user with userId " + userId + " to an office because a mapping already exists.");
		}
		if(officeIds.size() != 1){
			throw new Error("You can only add one user to one office.");
		}
		return super.mapPairLeftToRight(userId, officeIds);
	}
	
	public List<User> addUsersToOffice(Integer officeId, List<Integer> userIds) throws InstantiationException, IllegalAccessException {
		if(userIds.size() != 1){
			throw new Error("You can only add one user to one office.");
		}
		Integer userId = userIds.get(0);
		if(checkUserOfficeMapping(userId)){
			throw new Error("You cannot map user with userId " + userId + " to an office because a mapping already exists.");
		}
		
		return super.mapPairRightToLeft(officeId, userIds);
	}
	
	public List<User> getUsersByOffice(Integer skillId){
		return super.getLeftByRight(skillId);
	}
	
	public List<Office> getOfficesByUser(Integer userId){
		return super.getRightByLeft(userId);
	}
	
	public Boolean deleteUserOfficeMapping(Integer userId, Integer officeId){
		return super.deleteManyToManyMapping(userId, officeId);
	}
	
	private boolean checkUserOfficeMapping(Integer userId){
		String queryString = "from UserOffice where user.id = :id";
		Query query = super.getEM().createQuery(queryString);
		query.setParameter("id", userId);
		return query.getResultList().size() != 0;
	}
}
