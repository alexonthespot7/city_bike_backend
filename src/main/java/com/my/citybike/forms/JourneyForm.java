package com.my.citybike.forms;

public class JourneyForm {
	private Long departureStationId;
	private String departureTime;
	private Integer distance;
	private Integer duration;
	private Long returnStationId;
	private String returnTime;
	
	public Long getDepartureStationId() {
		return departureStationId;
	}
	public void setDepartureStationId(Long departureStationId) {
		this.departureStationId = departureStationId;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Long getReturnStationId() {
		return returnStationId;
	}
	public void setReturnStationId(Long returnStationId) {
		this.returnStationId = returnStationId;
	}
	public String getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}
}
