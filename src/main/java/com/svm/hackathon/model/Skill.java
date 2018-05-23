package com.svm.hackathon.model;
// Generated Apr 26, 2016 11:43:22 AM by Hibernate Tools 4.3.1.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.svm.hackathon.framework.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Skill generated by hbm2java
 */
@Entity
@Table(name = "skill", catalog="ebdb")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Skill implements java.io.Serializable, GenericEntity {

	private Integer id;
	private String skill;
	private String description;
	private Set<UserSkill> userSkills = new HashSet<UserSkill>(0);

	public Skill() {
	}

	public Skill(String skill, String description, Set<UserSkill> userSkills) {
		this.skill = skill;
		this.description = description;
		this.userSkills = userSkills;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "skill", length = 45)
	public String getSkill() {
		return this.skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	@JsonManagedReference(value = "user-skill-skill")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "skill")
	public Set<UserSkill> getUserSkills() {
		return this.userSkills;
	}

	public void setUserSkills(Set<UserSkill> userSkills) {
		this.userSkills = userSkills;
	}

}