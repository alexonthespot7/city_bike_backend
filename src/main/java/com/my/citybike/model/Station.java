package com.my.citybike.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Station {
	@Id
	@Column(nullable = false, updatable = false)
	private Long id;
	
	@Column
	private String address;
	
	@Column
	private String city;
	
	@Column
	private String latitude;
	
	@Column
	private String longitude;
	
	@Column
	private String name;
	
	@Column
	private String operator;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "returnStation")
	private List<Journey> journeysTo;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "departureStation")
	private List<Journey> journeysFrom;
	
	public Station() {}
	
	public Station(Long id, String address, String city, String latitude, String longitude, String name, String operator) {
		this.id = id;
		this.address = address;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.operator = operator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<Journey> getJourneysTo() {
		return journeysTo;
	}

	public void setJourneysTo(List<Journey> journeysTo) {
		this.journeysTo = journeysTo;
	}

	public List<Journey> getJourneysFrom() {
		return journeysFrom;
	}

	public void setJourneysFrom(List<Journey> journeysFrom) {
		this.journeysFrom = journeysFrom;
	}
}
