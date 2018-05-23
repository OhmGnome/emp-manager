package com.svm.hackathon.model;
// Generated Apr 26, 2016 11:43:22 AM by Hibernate Tools 4.3.1.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

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
 * Office generated by hbm2java
 */
@Entity
@Table(name = "office", catalog="ebdb")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Office implements java.io.Serializable, GenericEntity {

	private Integer id;
	private String name;
	private String address1;
	private String address2;
	private String icon;
	private Double lat;
	private Double lng;
	private Set<UserOffice> userOffices = new HashSet<UserOffice>(0);

	public Office() {
	}

	public Office(String name, String address1, String address2, String icon, Set<UserOffice> userOffices) {
		this.name = name;
		this.address1 = address1;
		this.address2 = address2;
		this.icon = icon;
		this.userOffices = userOffices;
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

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "address1", length = 45)
	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Column(name = "address2", length = 45)
	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(name = "icon", length = 45)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@JsonIgnore
	@JsonManagedReference(value = "user-office-office")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "office")
	public Set<UserOffice> getUserOffices() {
		return this.userOffices;
	}

	public void setUserOffices(Set<UserOffice> userOffices) {
		this.userOffices = userOffices;
	}

}