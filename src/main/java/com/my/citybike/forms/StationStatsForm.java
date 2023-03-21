package com.my.citybike.forms;

import java.util.List;

import com.my.citybike.model.Station;

public class StationStatsForm {
	private Long avgDistStartingFrom;
	private Long avgDistEndingAt;
	private List<Station> topPopReturnStations;
	private List<Station> topPopDepStations;
	
	public StationStatsForm() {}
	
	public StationStatsForm(Long avgDistStartingFrom, Long avgDistEndingAt, List<Station> topPopReturnStations,
			List<Station> topPopDepStations) {
		this.avgDistStartingFrom = avgDistStartingFrom;
		this.avgDistEndingAt = avgDistEndingAt;
		this.topPopReturnStations = topPopReturnStations;
		this.topPopDepStations = topPopDepStations;
	}

	public Long getAvgDistStartingFrom() {
		return avgDistStartingFrom;
	}

	public void setAvgDistStartingFrom(Long avgDistStartingFrom) {
		this.avgDistStartingFrom = avgDistStartingFrom;
	}

	public Long getAvgDistEndingAt() {
		return avgDistEndingAt;
	}

	public void setAvgDistEndingAt(Long avgDistEndingAt) {
		this.avgDistEndingAt = avgDistEndingAt;
	}

	public List<Station> getTopPopReturnStations() {
		return topPopReturnStations;
	}

	public void setTopPopReturnStations(List<Station> topPopReturnStations) {
		this.topPopReturnStations = topPopReturnStations;
	}

	public List<Station> getTopPopDepStations() {
		return topPopDepStations;
	}

	public void setTopPopDepStations(List<Station> topPopDepStations) {
		this.topPopDepStations = topPopDepStations;
	}
}
