package com.my.citybike.model;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends CrudRepository<Journey, Long> {
	@Query(value = "SELECT COUNT(*) FROM journey WHERE departure_station_id = ?1", nativeQuery = true)
	int journeysFromByStationId(Long stationId);
	
	@Query(value = "SELECT COUNT(*) FROM journey WHERE return_station_id = ?1", nativeQuery = true)
	int journeysToByStationId(Long stationId);
	
	@Query(value = "SELECT AVG(distance) FROM journey WHERE departure_station_id = ?1", nativeQuery = true)
	Double avgDistanceByDepartureStationId(Long departureStationId);
	
	@Query(value = "SELECT AVG(distance) FROM journey WHERE return_station_id = ?1", nativeQuery = true)
	Double avgDistanceByReturnStationId(Long returnStationId);
	
	@Query(value = "SELECT return_station_id FROM journey WHERE departure_station_id = ?1 GROUP BY return_station_id ORDER BY COUNT(*) DESC LIMIT 5", nativeQuery = true)
	List<Long> topPopReturnByDepartureStationId(Long departureStationId);
	
	@Query(value = "SELECT departure_station_id FROM journey WHERE return_station_id = ?1 GROUP BY departure_station_id ORDER BY COUNT(*) DESC LIMIT 5", nativeQuery = true)
	List<Long> topPopDepartureByReturnStationId(Long returnStationId);
}
