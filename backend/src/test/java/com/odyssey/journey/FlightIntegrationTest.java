package com.odyssey.journey;


import com.github.javafaker.Faker;

import com.odyssey.flights.Flight;
import com.odyssey.flights.FlightNamingService;
import com.odyssey.flights.FlightRegistrationRequest;
import com.odyssey.flights.FlightUpdateRequest;
import com.odyssey.locations.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FlightIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    private static final String FLIGHT_URI = "/api/v1/flights";
    private static final String LOCATION_URI = "/api/v1/locations";

    private Location setUpLocation() {
        Faker faker = new Faker();
        String city = faker.name().fullName();
        String country = city;
        String picture = "pic";
        LocationRegistrationRequest request = new LocationRegistrationRequest(
                city,
                country,
                picture
        );
        webTestClient.post()
                .uri(LOCATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LocationRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        List<Location> allLocations = webTestClient.get()
                .uri(LOCATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Location>() {})
                .returnResult()
                .getResponseBody();

        int id = allLocations.stream()
                .filter(location -> location.getCity().equals(city) && location.getCountry().equals(country))
                .map(Location::getId)
                .findFirst()
                .orElseThrow();
        return new Location(id, city, country, picture);
    }

    @Test
    void canRegisterAFlight() {
        Faker faker = new Faker();
        LocalDateTime time = LocalDateTime.now();
        Location origin = setUpLocation();
        Location destination = setUpLocation();

        String name = FlightNamingService.getFlightName(origin, destination, time);

        FlightRegistrationRequest request = new FlightRegistrationRequest(
                time, origin.getId(), destination.getId()
        );
        webTestClient.post()
                .uri(FLIGHT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FlightRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Flight> allFlights = webTestClient.get()
                .uri(FLIGHT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Flight>() {})
                .returnResult()
                .getResponseBody();

        Flight expectedFlight = new Flight(
                name, time, origin, destination
        );

        assertThat(allFlights)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedFlight);

        int id = allFlights.stream()
                .filter(flight -> flight.getName().equals(name) && flight.getOrigin().equals(origin) && flight.getDestination().equals(destination))
                .map(Flight::getId)
                .findFirst()
                .orElseThrow();

        expectedFlight.setId(id);

        webTestClient.get()
                .uri(FLIGHT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Flight>() {})
                .isEqualTo(expectedFlight);

    }

    @Test
    void canDeleteFlight() {
        Faker faker = new Faker();
        LocalDateTime time = LocalDateTime.now();
        Location origin = setUpLocation();
        Location destination = setUpLocation();
        String name = FlightNamingService.getFlightName(origin, destination, time);

        FlightRegistrationRequest request = new FlightRegistrationRequest(
                time, origin.getId(), destination.getId()
        );
        webTestClient.post()
                .uri(FLIGHT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FlightRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Flight> allFlights = webTestClient.get()
                .uri(FLIGHT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Flight>() {})
                .returnResult()
                .getResponseBody();

        Flight expectedFlight = new Flight(
                name,time, origin, destination
        );

        assertThat(allFlights)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedFlight);

        int id = allFlights.stream()
                .filter(flight -> flight.getName().equals(name) && flight.getOrigin().equals(origin) && flight.getDestination().equals(destination))
                .map(Flight::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(FLIGHT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(FLIGHT_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateFlightAllFields() {
        Faker faker = new Faker();
        LocalDateTime time = LocalDateTime.now();
        Location origin = setUpLocation();
        Location destination = setUpLocation();
        String name = FlightNamingService.getFlightName(origin, destination, time);

        FlightRegistrationRequest request = new FlightRegistrationRequest(
                time, origin.getId(), destination.getId()
        );
        webTestClient.post()
                .uri(FLIGHT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FlightRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Flight> allFlights = webTestClient.get()
                .uri(FLIGHT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Flight>() {})
                .returnResult()
                .getResponseBody();


        int id = allFlights.stream()
                .filter(flight -> flight.getName().equals(name) && flight.getOrigin().equals(origin) && flight.getDestination().equals(destination))
                .map(Flight::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        LocalDateTime time2 = time.plus(90, ChronoUnit.DAYS);
        Location origin2 = setUpLocation();
        Location destination2 = setUpLocation();;
        FlightUpdateRequest updateRequest = new FlightUpdateRequest(
                time2, origin2.getId(), destination2.getId()
        );

        webTestClient.put()
                .uri(FLIGHT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), FlightUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Flight updatedFlight = webTestClient.get()
                .uri(FLIGHT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Flight.class)
                .returnResult()
                .getResponseBody();

        String name2 = FlightNamingService.getFlightName(origin2, destination2, time2);

        Flight expected = new Flight(
                id, name2, time2, origin2, destination2
        );
        assertThat(updatedFlight).isEqualTo(expected);
    }
}
