package com.odyssey.flights;

import com.odyssey.events.Event;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    @Mock
    private FlightDao flightDao;
    @Mock
    private LocationDao locationDao;
    private FlightService underTest;

    @BeforeEach
    void setUp() {
        underTest = new FlightService(flightDao, locationDao);
    }

    @Test
    void getAllFlights() {
        underTest.getAllFlights();
        verify(flightDao).selectAllFlights();
    }

    @Test
    void getFlightById() {
        int id = 1;
        Location origin = new Location();
        Location destination = new Location();
        Timestamp timestamp = new Timestamp(1000);
        String flightName = "A-B@1000";

        Flight flight = new Flight(id, flightName, timestamp, origin, destination);
        when(flightDao.selectFlightById(id)).thenReturn(Optional.of(flight));

        Flight actual = underTest.getFlight(id);
        assertThat(actual).isEqualTo(flight);
    }

    @Test
    void willThrowWhenGetFlightByIdEmptyOptional() {
        int id = 1;
        when(flightDao.selectFlightById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getFlight(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("flight with id [%s] not found".formatted(id));
    }

    @Test
    void getFlightsByOriginId() {
        int id = 1;
        Location origin = new Location();
        origin.setId(1);

        when(locationDao.existsLocationById(id)).thenReturn(true);

        underTest.getFlightsByOriginId(origin.getId());
        verify(flightDao).selectFlightsByOriginId(origin.getId());
    }

    @Test
    void willThrowWhenGetFlightsByOriginIdLocationNotExists() {
        int id = 1;
        when(locationDao.existsLocationById(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.getFlightsByOriginId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(id));

        verify(flightDao, never()).selectFlightsByOriginId(any());
    }

    @Test
    void getFlightsByDestinationId() {
        int id = 1;
        Location destination = new Location();
        destination.setId(id);

        when(locationDao.existsLocationById(id)).thenReturn(true);

        underTest.getFlightsByDestinationId(destination.getId());
        verify(flightDao).selectFlightsByDestinationId(destination.getId());
    }

    @Test
    void willThrowWhenGetFlightsByDestinationIdLocationNotExists() {
        int id = 1;
        when(locationDao.existsLocationById(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.getFlightsByDestinationId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(id));

        verify(flightDao, never()).selectFlightsByDestinationId(any());
    }

    @Test
    void addFlight() {
        int id = 1;
        Location origin = new Location(1, "Peja", "Kosova", "pic");
        Location destination = new Location(2, "Munich", "Germany", "pic");
        Timestamp timestamp = new Timestamp(1000);
        String flightName = FlightNamingService.getFlightName(origin, destination, timestamp);

        Flight flight = new Flight(id, flightName, timestamp, origin, destination);
        when(locationDao.selectLocationById(origin.getId())).thenReturn(Optional.of(origin));
        when(locationDao.selectLocationById(destination.getId())).thenReturn(Optional.of(destination));
        when(flightDao.existsFlightByName(flightName)).thenReturn(false);

        FlightRegistrationRequest request = new FlightRegistrationRequest(
                timestamp, origin.getId(), destination.getId()
        );

        underTest.addFlight(request);

        ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
        verify(flightDao).insertFlight(flightArgumentCaptor.capture());

        Flight capturedFlight = flightArgumentCaptor.getValue();
        assertThat(capturedFlight.getId()).isNull();
        assertThat(capturedFlight.getName()).isEqualTo(flightName);
        assertThat(capturedFlight.getOrigin().getId()).isEqualTo(request.originId());
        assertThat(capturedFlight.getDestination().getId()).isEqualTo(request.destinationId());
        assertThat(capturedFlight.getTime()).isEqualTo(request.departure());
    }

    @Test
    void willThrowWhenAddFlightOriginNotFound() {
        int id = 1;
        Location origin = new Location(1, "Peja", "Kosova", "pic");
        Location destination = new Location(2, "Munich", "Germany", "pic");
        Timestamp timestamp = new Timestamp(1000);
        String flightName = FlightNamingService.getFlightName(origin, destination, timestamp);

        Flight flight = new Flight(id, flightName, timestamp, origin, destination);
        when(locationDao.selectLocationById(origin.getId())).thenReturn(Optional.empty());
        lenient().when(locationDao.selectLocationById(destination.getId())).thenReturn(Optional.of(destination));
        lenient().when(flightDao.existsFlightByName(flightName)).thenReturn(false);

        FlightRegistrationRequest request = new FlightRegistrationRequest(
                timestamp, origin.getId(), destination.getId()
        );

        assertThatThrownBy(() -> underTest.addFlight(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(origin.getId()));
        verify(flightDao, never()).insertFlight(any());
    }

    @Test
    void willThrowWhenAddFlightDestinationNotFound() {
        int id = 1;
        Location origin = new Location(1, "Peja", "Kosova", "pic");
        Location destination = new Location(2, "Munich", "Germany", "pic");
        Timestamp timestamp = new Timestamp(1000);
        String flightName = FlightNamingService.getFlightName(origin, destination, timestamp);

        Flight flight = new Flight(id, flightName, timestamp, origin, destination);
        lenient().when(locationDao.selectLocationById(origin.getId())).thenReturn(Optional.of(origin));
        when(locationDao.selectLocationById(destination.getId())).thenReturn(Optional.empty());
        lenient().when(flightDao.existsFlightByName(flightName)).thenReturn(false);

        FlightRegistrationRequest request = new FlightRegistrationRequest(
                timestamp, origin.getId(), destination.getId()
        );

        assertThatThrownBy(() -> underTest.addFlight(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(destination.getId()));
        verify(flightDao, never()).insertFlight(any());
    }

    @Test
    void willThrowWhenAddFlightOriginAndDestinationEqual() {
        int id = 1;
        Location origin = new Location(1, "Peja", "Kosova", "pic");
        Timestamp timestamp = new Timestamp(1000);
        String flightName = FlightNamingService.getFlightName(origin, origin, timestamp);

        Flight flight = new Flight(id, flightName, timestamp, origin, origin);
        lenient().when(locationDao.selectLocationById(origin.getId())).thenReturn(Optional.of(origin));
        lenient().when(flightDao.existsFlightByName(flightName)).thenReturn(false);

        FlightRegistrationRequest request = new FlightRegistrationRequest(
                timestamp, origin.getId(), origin.getId()
        );

        assertThatThrownBy(() -> underTest.addFlight(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("origin and destination must be different");
        verify(flightDao, never()).insertFlight(any());
    }

    @Test
    void deleteFlight() {
        int id = 1;
        when(flightDao.existsFlightById(id)).thenReturn(true);
        underTest.deleteFlight(id);
        verify(flightDao).deleteFlightById(id);
    }

    @Test
    void willThrowDeleteFlightByIdNotExists() {
        int id = 1;
        when(flightDao.existsFlightById(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.deleteFlight(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("flight with id [%s] not found".formatted(id));
        verify(flightDao, never()).deleteFlightById(any());
    }

    @Test
    void updateFlight() {
        int id = 1;
        Location origin = new Location(1, "Peja", "Kosova", "pic");
        Location destination = new Location(2, "Munich", "Germany", "pic");
        Timestamp timestamp = new Timestamp(1000);
        String flightName = FlightNamingService.getFlightName(origin, destination, timestamp);

        Flight flight = new Flight(id, flightName, timestamp, origin, destination);

        when(flightDao.selectFlightById(id)).thenReturn(Optional.of(flight));

        Location newOrigin = destination;
        Location newDestination = origin;
        Timestamp newTimestamp = new Timestamp(2000);
        String newFlightName = FlightNamingService.getFlightName(newOrigin, newDestination, newTimestamp);

        when(flightDao.existsFlightByName(newFlightName)).thenReturn(false);
        when(locationDao.selectLocationById(origin.getId())).thenReturn(Optional.of(origin));
        when(locationDao.selectLocationById(destination.getId())).thenReturn(Optional.of(destination));

        FlightUpdateRequest request = new FlightUpdateRequest(
                newTimestamp, newOrigin.getId(), newDestination.getId()
        );

        underTest.updateFlight(id, request);

        ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
        verify(flightDao).updateFlight(flightArgumentCaptor.capture());

        Flight capturedFlight = flightArgumentCaptor.getValue();
        assertThat(capturedFlight.getId()).isEqualTo(id);
        assertThat(capturedFlight.getName()).isEqualTo(newFlightName);
        assertThat(capturedFlight.getOrigin().getId()).isEqualTo(request.originId());
        assertThat(capturedFlight.getDestination().getId()).isEqualTo(request.destinationId());
        assertThat(capturedFlight.getTime()).isEqualTo(request.departure());
    }
}