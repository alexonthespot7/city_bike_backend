package com.my.citybike.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Journey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;
	
	@Column
	private String departureTime;
	
	@Column
	private String returnTime;
	
	@Column
	private int distance;
	
	@Column
	private int duration;
	
	@ManyToOne
	@JoinColumn(name = "return_station_id", referencedColumnName = "id")
	private Station returnStation;
	
	@ManyToOne
	@JoinColumn(name = "departur_station_id", referencedColumnName = "id")
	private Station departureStation;
	
	public Journey() {}

	public Journey(String departureTime, String returnTime, int distance, int duration, Station returnStation,
			Station departureStation) {
		super();
		this.departureTime = departureTime;
		this.returnTime = returnTime;
		this.distance = distance;
		this.duration = duration;
		this.returnStation = returnStation;
		this.departureStation = departureStation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Station getReturnStation() {
		return returnStation;
	}

	public void setReturnStation(Station returnStation) {
		this.returnStation = returnStation;
	}

	public Station getDepartureStation() {
		return departureStation;
	}

	public void setDepartureStation(Station departureStation) {
		this.departureStation = departureStation;
	}
}
