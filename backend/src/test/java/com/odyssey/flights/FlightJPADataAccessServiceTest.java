package com.odyssey.flights;

import com.odyssey.locations.Location;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;

public class FlightJPADataAccessServiceTest {

    private FlightJPADataAccessService flightJPADataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        flightJPADataAccessService = new FlightJPADataAccessService(flightRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetAllFlights() {
        flightJPADataAccessService.selectAllFlights();
        verify(flightRepository).findAll();
    }

    @Test
    void testGetFlightById() {
        int id = 1;
        flightJPADataAccessService.selectFlightById(id);
        verify(flightRepository).findById(id);
    }

    @Test
    void testGetFlightsByOriginId() {
        int id = 1;
        flightJPADataAccessService.selectFlightsByOriginId(id);
        verify(flightRepository).findByOriginId(id);
    }

    @Test
    void testGetFlightsByDestinationId() {
        int id = 1;
        flightJPADataAccessService.selectFlightsByDestinationId(id);
        verify(flightRepository).findByDestinationId(id);
    }

    @Test
    void insertFlight() {
        Location origin = new Location();
        Location destination = new Location();
        Timestamp timestamp = new Timestamp(1000);
        String flightName = "A-B@1000";

        Flight flight = new Flight(flightName, timestamp, origin, destination);

        flightJPADataAccessService.insertFlight(flight);
        verify(flightRepository).save(flight);
    }

    @Test
    void updateFlight() {
        Location origin = new Location();
        Location destination = new Location();
        Timestamp timestamp = new Timestamp(1000);
        String flightName = "A-B@1000";

        Flight flight = new Flight(flightName, timestamp, origin, destination);

        flightJPADataAccessService.updateFlight(flight);
        verify(flightRepository).save(flight);
    }

    @Test
    void existsFlightById() {
        int id = 1;
        flightJPADataAccessService.existsFlightById(id);
        verify(flightRepository).existsFlightById(id);
    }

    @Test
    void existsFlightByName() {
        String name = "a";
        flightJPADataAccessService.existsFlightByName(name);
        verify(flightRepository).existsFlightByName(name);
    }

    @Test
    void deleteFlightById() {
        int id = 1;
        flightJPADataAccessService.deleteFlightById(id);
        verify(flightRepository).deleteById(id);
    }

}
