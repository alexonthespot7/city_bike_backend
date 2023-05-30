package com.my.citybike;

import com.my.citybike.model.Station;
import com.my.citybike.model.StationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveStation() {
        // Test saving a station
        Station station = new Station(1L, "123 Main St", "City", "123.456", "789.012", "Station 1", "Operator 1");
        Station savedStation = stationRepository.save(station);

        Assertions.assertNotNull(savedStation.getId());
        Assertions.assertEquals(station.getAddress(), savedStation.getAddress());
        Assertions.assertEquals(station.getCity(), savedStation.getCity());
        Assertions.assertEquals(station.getLatitude(), savedStation.getLatitude());
        Assertions.assertEquals(station.getLongitude(), savedStation.getLongitude());
        Assertions.assertEquals(station.getName(), savedStation.getName());
        Assertions.assertEquals(station.getOperator(), savedStation.getOperator());
    }

    @Test
    public void testFindById() {
        // Test finding a station by ID
        Station station = new Station(1L, "123 Main St", "City", "123.456", "789.012", "Station 1", "Operator 1");
        entityManager.persist(station);

        Optional<Station> foundStation = stationRepository.findById(station.getId());

        Assertions.assertTrue(foundStation.isPresent());
        Assertions.assertEquals(station.getAddress(), foundStation.get().getAddress());
        Assertions.assertEquals(station.getCity(), foundStation.get().getCity());
        Assertions.assertEquals(station.getLatitude(), foundStation.get().getLatitude());
        Assertions.assertEquals(station.getLongitude(), foundStation.get().getLongitude());
        Assertions.assertEquals(station.getName(), foundStation.get().getName());
        Assertions.assertEquals(station.getOperator(), foundStation.get().getOperator());
    }

    @Test
    public void testFindAll() {
        // Test finding all stations
        Station station1 = new Station(1L, "123 Main St", "City", "123.456", "789.012", "Station 1", "Operator 1");
        Station station2 = new Station(2L, "456 Elm St", "City", "456.789", "012.345", "Station 2", "Operator 2");
        entityManager.persist(station1);
        entityManager.persist(station2);

        List<Station> stations = (List<Station>) stationRepository.findAll();

        Assertions.assertEquals(2, stations.size());
    }

    @Test
    public void testDeleteStation() {
        // Test deleting a station
        Station station = new Station(1L, "123 Main St", "City", "123.456", "789.012", "Station 1", "Operator 1");
        entityManager.persist(station);

        stationRepository.delete(station);

        Optional<Station> deletedStation = stationRepository.findById(station.getId());

        Assertions.assertFalse(deletedStation.isPresent());
    }
}
