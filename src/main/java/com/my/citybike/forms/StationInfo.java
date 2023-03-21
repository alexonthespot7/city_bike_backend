package com.my.citybike.forms;

public class StationInfo {
	private Long id;
	private Integer journeysFrom;
	private Integer journeysTo;
	private String address;
	private String latitude;
	private String longitude;
	private String name;
	
	public StationInfo() {}
	
	public StationInfo(Long id, Integer journeysFrom, Integer journeysTo, String address, String latitude, String longitude,
			String name) {
		this.id = id;
		this.journeysFrom = journeysFrom;
		this.journeysTo = journeysTo;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getJourneysFrom() {
		return journeysFrom;
	}

	public void setJourneysFrom(Integer journeysFrom) {
		this.journeysFrom = journeysFrom;
	}

	public Integer getJourneysTo() {
		return journeysTo;
	}

	public void setJourneysTo(Integer journeysTo) {
		this.journeysTo = journeysTo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
}
