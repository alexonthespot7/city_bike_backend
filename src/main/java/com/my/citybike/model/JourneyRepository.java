package com.my.citybike.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends CrudRepository<Journey, Long> {
	@Query(value = "SELECT * FROM journey WHERE departure_time = ?1 AND return_time = ?2 AND distance = ?3 AND duration = ?4 AND return_station_id = ?5 AND departure_station_id = ?6", nativeQuery = true)
	Optional<Journey> findByFields(Date departureTime, Date returnTime, int distance, int duration, Long returnStationId, Long departureStationId);
	
	@Query(value = "SELECT COUNT(*) FROM journey WHERE departure_station_id = ?1", nativeQuery = true)
	int journeysFromByStationId(Long stationId);
	
	@Query(value = "SELECT COUNT(*) FROM journey WHERE return_station_id = ?1", nativeQuery = true)
	int journeysToByStationId(Long stationId);
	
	@Query(value = "SELECT AVG(distance) FROM journey WHERE departure_station_id = ?1", nativeQuery = true)
	Optional<Double> avgDistanceByDepartureStationId(Long departureStationId);
	
	@Query(value = "SELECT AVG(distance) FROM journey WHERE return_station_id = ?1", nativeQuery = true)
	Optional<Double> avgDistanceByReturnStationId(Long returnStationId);
	
	@Query(value = "SELECT return_station_id FROM journey WHERE departure_station_id = ?1 GROUP BY return_station_id ORDER BY COUNT(*) DESC LIMIT 5", nativeQuery = true)
	List<Long> topPopReturnByDepartureStationId(Long departureStationId);
	
	@Query(value = "SELECT departure_station_id FROM journey WHERE return_station_id = ?1 GROUP BY departure_station_id ORDER BY COUNT(*) DESC LIMIT 5", nativeQuery = true)
	List<Long> topPopDepartureByReturnStationId(Long returnStationId);
	
	@Query(value = "SELECT AVG(distance) FROM journey WHERE departure_station_id = ?1 AND departure_time > ?2 AND departure_time < ?3", nativeQuery = true)
	Optional<Double> avgDistanceByDepartureStationIdAndMonth(Long departureStationId, Date firstDay, Date secondDay);
	
	@Query(value = "SELECT AVG(distance) FROM journey WHERE return_station_id = ?1 AND return_time > ?2 AND return_time < ?3", nativeQuery = true)
	Optional<Double> avgDistanceByReturnStationIdAndMonth(Long returnStationId, Date firstDay, Date secondDAy);
	
	@Query(value = "SELECT return_station_id FROM journey WHERE departure_station_id = ?1 AND departure_time > ?2 AND departure_time < ?3 GROUP BY return_station_id ORDER BY COUNT(*) DESC LIMIT 5", nativeQuery = true)
	List<Long> topPopReturnByDepartureStationIdAndMonth(Long departureStationId, Date firstDay, Date secondDay);
	
	@Query(value = "SELECT departure_station_id FROM journey WHERE return_station_id = ?1 AND return_time > ?2 AND return_time < ?3 GROUP BY departure_station_id ORDER BY COUNT(*) DESC LIMIT 5", nativeQuery = true)
	List<Long> topPopDepartureByReturnStationIdAndMonth(Long returnStationId, Date firstDay, Date secondDay);
	
	@Query(value = "SELECT * FROM journey WHERE departure_station_id = ?1 AND departure_time > ?2 AND departure_time < ?3", nativeQuery = true)
	List<Journey> findJourneysFromByDepartureStationIdAndMonth(Long departureStationId, Date firstDay, Date secondDay);
	
	@Query(value = "SELECT * FROM journey WHERE return_station_id = ?1 AND return_time > ?2 AND return_time < ?3", nativeQuery = true)
	List<Journey> findJourneysToByReturnStationIdAndMonth(Long returnStationId, Date firstDay, Date secondDay);
}
