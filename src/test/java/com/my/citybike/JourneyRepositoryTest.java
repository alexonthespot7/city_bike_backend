package com.my.citybike;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.my.citybike.model.Journey;
import com.my.citybike.model.JourneyRepository;
import com.my.citybike.model.Station;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class JourneyRepositoryTest {

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setup() {
        // Create and persist sample data for testing
        Station station1 = new Station();
        Station station2 = new Station();
        entityManager.persist(station1);
        entityManager.persist(station2);

        Date departureTime = Date.valueOf("2023-01-01");
        Date returnTime = Date.valueOf("2023-01-02");
        int distance = 10;
        int duration = 5;
        Journey journey1 = new Journey(departureTime, returnTime, distance, duration, station1, station2);
        Journey journey2 = new Journey(departureTime, returnTime, distance, duration, station1, station2);
        entityManager.persist(journey1);
        entityManager.persist(journey2);
        entityManager.flush();
    }

    @Test
    public void testDeleteDuplicates() {
        journeyRepository.deleteDuplicates();
        List<Journey> journeys = (List<Journey>) journeyRepository.findAll();

        // Verify that duplicates are deleted
        Assertions.assertEquals(1, journeys.size());
    }

    @Test
    public void testFindByFields() {
        Date departureTime = Date.valueOf("2023-01-01");
        Date returnTime = Date.valueOf("2023-01-02");
        int distance = 10;
        int duration = 5;
        Long returnStationId = 1L;
        Long departureStationId = 2L;

        List<Journey> result = journeyRepository.findByFields(departureTime, returnTime, distance, duration, returnStationId, departureStationId);

        // Verify that the correct journeys are retrieved
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testJourneysFromByStationId() {
        Long stationId = 1L;

        int count = journeyRepository.journeysFromByStationId(stationId);

        // Verify the count of journeys from the station
        Assertions.assertEquals(2, count);
    }

    @Test
    public void testJourneysToByStationId() {
        Long stationId = 2L;

        int count = journeyRepository.journeysToByStationId(stationId);

        // Verify the count of journeys to the station
        Assertions.assertEquals(2, count);
    }

    @Test
    public void testAvgDistanceByDepartureStationId() {
        Long departureStationId = 1L;

        Optional<Double> avgDistance = journeyRepository.avgDistanceByDepartureStationId(departureStationId);

        // Verify the average distance for journeys from the departure station
        Assertions.assertTrue(avgDistance.isPresent());
        Assertions.assertEquals(10.0, avgDistance.get(), 0.01);
    }

    @Test
    public void testAvgDistanceByReturnStationId() {
        Long returnStationId = 2L;

        Optional<Double> avgDistance = journeyRepository.avgDistanceByReturnStationId(returnStationId);

        // Verify the average distance for journeys to the return station
        Assertions.assertTrue(avgDistance.isPresent());
        Assertions.assertEquals(10.0, avgDistance.get(), 0.01);
    }

    @Test
    public void testTopPopReturnByDepartureStationId() {
        Long departureStationId = 1L;

        List<Long> topPopReturn = journeyRepository.topPopReturnByDepartureStationId(departureStationId);

        // Verify the list of top popular return stations from the departure station
        Assertions.assertEquals(0, topPopReturn.size());
    }

    @Test
    public void testTopPopDepartureByReturnStationId() {
        Long returnStationId = 2L;

        List<Long> topPopDeparture = journeyRepository.topPopDepartureByReturnStationId(returnStationId);

        // Verify the list of top popular departure stations from the return station
        Assertions.assertEquals(0, topPopDeparture.size());
    }

    @Test
    public void testAvgDistanceByDepartureStationIdAndMonth() {
        Long departureStationId = 1L;
        Date firstDay = Date.valueOf("2023-01-01");
        Date secondDay = Date.valueOf("2023-01-31");

        Optional<Double> avgDistance = journeyRepository.avgDistanceByDepartureStationIdAndMonth(departureStationId, firstDay, secondDay);

        // Verify the average distance for journeys from the departure station within the specified month
        Assertions.assertFalse(avgDistance.isPresent());
    }

    @Test
    public void testAvgDistanceByReturnStationIdAndMonth() {
        Long returnStationId = 2L;
        Date firstDay = Date.valueOf("2023-01-01");
        Date secondDay = Date.valueOf("2023-01-31");

        Optional<Double> avgDistance = journeyRepository.avgDistanceByReturnStationIdAndMonth(returnStationId, firstDay, secondDay);

        // Verify the average distance for journeys to the return station within the specified month
        Assertions.assertFalse(avgDistance.isPresent());
    }

    @Test
    public void testTopPopReturnByDepartureStationIdAndMonth() {
        Long departureStationId = 1L;
        Date firstDay = Date.valueOf("2023-01-01");
        Date secondDay = Date.valueOf("2023-01-31");

        List<Long> topPopReturn = journeyRepository.topPopReturnByDepartureStationIdAndMonth(departureStationId, firstDay, secondDay);

        // Verify the list of top popular return stations from the departure station within the specified month
        Assertions.assertEquals(0, topPopReturn.size());
    }

    @Test
    public void testTopPopDepartureByReturnStationIdAndMonth() {
        Long returnStationId = 2L;
        Date firstDay = Date.valueOf("2023-01-01");
        Date secondDay = Date.valueOf("2023-01-31");

        List<Long> topPopDeparture = journeyRepository.topPopDepartureByReturnStationIdAndMonth(returnStationId, firstDay, secondDay);

        // Verify the list of top popular departure stations from the return station within the specified month
        Assertions.assertEquals(0, topPopDeparture.size());
    }

    @Test
    public void testFindJourneysFromByDepartureStationIdAndMonth() {
        Long departureStationId = 1L;
        Date firstDay = Date.valueOf("2023-01-01");
        Date secondDay = Date.valueOf("2023-01-31");

        List<Journey> journeys = journeyRepository.findJourneysFromByDepartureStationIdAndMonth(departureStationId, firstDay, secondDay);

        // Verify the list of journeys from the departure station within the specified month
        Assertions.assertEquals(2, journeys.size());
    }

    @Test
    public void testFindJourneysToByReturnStationIdAndMonth() {
        Long returnStationId = 2L;
        Date firstDay = Date.valueOf("2023-01-01");
        Date secondDay = Date.valueOf("2023-01-31");

        List<Journey> journeys = journeyRepository.findJourneysToByReturnStationIdAndMonth(returnStationId, firstDay, secondDay);

        // Verify the list of journeys to the return station within the specified month
        Assertions.assertEquals(2, journeys.size());
    }

}