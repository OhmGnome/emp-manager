package com.svm.hackathon.datalayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svm.hackathon.framework.GenericManyToManyDAO;
import com.svm.hackathon.model.Skill;
import com.svm.hackathon.model.User;
import com.svm.hackathon.model.UserSkill;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public class UserSkillDAO extends GenericManyToManyDAO<UserSkill, User, Skill> {
	
	private Class joinClass = UserSkill.class;
	private Class leftClass = User.class;
	private Class rightClass = Skill.class;
	
	private String joinQueryString = "UserSkill";
	private String leftQueryString = "user";
	private String rightQueryString = "skill";
	
	@PostConstruct
	public void init(){
		super.setClasses(joinClass, leftClass, rightClass);
		super.setIdFields(joinQueryString, leftQueryString, rightQueryString);
	}
	
	public List<Skill> addSkillsToUser(Integer userId, List<Integer> skillIds) throws InstantiationException, IllegalAccessException{
		List<Integer> userIds = new ArrayList<Integer>();
		userIds.add(userId);
		if(checkUserSkillMapping(userIds, skillIds)){
			throw new Error("You are trying to map a duplicate skill to user: " + userId + ".");
		}
		return super.mapPairLeftToRight(userId, skillIds);
	}
	
	public List<User> addUsersToSkill(Integer skillId, List<Integer> userIds) throws InstantiationException, IllegalAccessException{
		List<Integer> skillIds = new ArrayList<Integer>();
		skillIds.add(skillId);
		if(checkUserSkillMapping(userIds, skillIds)){
			throw new Error("You are trying to map a dulplicate user to skill: " + skillId + ".");
		}
		return super.mapPairRightToLeft(skillId, userIds);
	}
	
	public List<User> getUserBySkill(Integer skillId){
		return super.getLeftByRight(skillId);
	}
	
	public List<Skill> getSkillByUser(Integer userId){
		return super.getRightByLeft(userId);
	}
	
	public Boolean unmapUserSkill(Integer userId, Integer skillId){
		return super.deleteManyToManyMapping(userId, skillId);
	}
	
	public List<Map<String, Object>> getSkillsByUserSkillId(Integer userId){
		String query = "from UserSkill u where u.user.id = :id order by u.timestamp";
		List<UserSkill> userSkills = (List<UserSkill>)super.getEM().createQuery(query).setParameter("id", userId).getResultList();
		List<Map<String, Object>> returnSkills = new ArrayList<Map<String, Object>>();
		for(UserSkill userSkill: userSkills){
			Map<String, Object> mappedSkill = new HashMap<String, Object>();
			mappedSkill.put("timestamp", userSkill.getTimestamp());
			mappedSkill.put("skill", userSkill.getSkill());
			returnSkills.add(mappedSkill);
		}
		return returnSkills;
	}
	
	private boolean checkUserSkillMapping(List<Integer> userIds, List<Integer> skillIds){
		StringBuilder queryString = new StringBuilder();
		queryString.append("from UserSkill u where ");
		for(int i = 0; i < userIds.size(); i++){
			for(int j = 0; j < skillIds.size(); j++){
				if(i != 0 || j != 0){
					queryString.append(" or ");
				}
				queryString.append("u." + leftQueryString + ".id = :" + leftQueryString + i + j + " and " + "u." + rightQueryString + ".id = :" + rightQueryString + i + j);
			}
		}
		Query query = super.getEM().createQuery(queryString.toString());
		for(int k = 0; k < userIds.size(); k++){
			for(int l = 0; l < skillIds.size(); l++){
				query.setParameter(leftQueryString + k + l, userIds.get(k));
				query.setParameter(rightQueryString + k + l, skillIds.get(l));
			}
		}
		return 0 != query.getResultList().size();
	}
	
}
